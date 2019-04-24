package com.github.richygreat.microbankbff.user.service;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.github.richygreat.microbankbff.stream.KafkaChannel;
import com.github.richygreat.microbankbff.stream.KafkaConstants;
import com.github.richygreat.microbankbff.stream.KafkaEventConstants;
import com.github.richygreat.microbankbff.stream.KafkaMessageUtility;
import com.github.richygreat.microbankbff.stream.Source;
import com.github.richygreat.microbankbff.stream.exception.EventPushFailedException;
import com.github.richygreat.microbankbff.user.entity.UserEntity;
import com.github.richygreat.microbankbff.user.exception.UserAlreadyExistsException;
import com.github.richygreat.microbankbff.user.model.UserDTO;
import com.github.richygreat.microbankbff.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	private final Source source;
	private final UserRepository userRepository;
	private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;
	private ReadOnlyKeyValueStore<String, UserDTO> userSnapshotStore;

	@Transactional
	public void createUser(UserDTO userDTO) {
		Optional<UserEntity> optionalUser = userRepository.findByUserName(userDTO.getUserName());
		if (optionalUser.isPresent()) {
			throw new UserAlreadyExistsException();
		}
		userDTO.setId(UUID.randomUUID().toString());
		boolean sent = source.userProducer().send(KafkaMessageUtility.createMessage(userDTO,
				KafkaEventConstants.USER_CREATION_REQUESTED, userDTO.getId(), userDTO.getUserName()));
		log.info("createUser: Exiting userDTO: {} sent: {}", userDTO.getId(), sent);
		if (!sent) {
			throw new EventPushFailedException();
		}
	}

	@Transactional
	@StreamListener(value = KafkaChannel.USER_SINK_CHANNEL, condition = KafkaEventConstants.USER_CREATED_HEADER)
	public void handleUserCreated(@Payload UserDTO userDTO, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
		log.info("handleUserCreated: Entering: userDTO: {} partition: {}", userDTO, partition);
		Optional<UserEntity> optionalUser = userRepository.findByUserName(userDTO.getUserName());
		if (optionalUser.isPresent()) {
			throw new UserAlreadyExistsException();
		}
		UserEntity user = new UserEntity();
		user.setId(userDTO.getId());
		user.setUserName(userDTO.getUserName());
		user.setTaxId(userDTO.getTaxId());
		userRepository.save(user);
	}

	@Transactional
	@StreamListener(value = KafkaChannel.USER_SINK_CHANNEL, condition = KafkaEventConstants.USER_CREATION_FAILED_HEADER)
	public void handleUserCreationFailed(@Payload UserDTO userDTO,
			@Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
		log.info("handleUserCreationFailed: Entering: userDTO: {} partition: {}", userDTO, partition);
		// Write this as a rest log
	}

	public UserDTO getUser(String id) {
		Optional<UserEntity> optionalUser = userRepository.findById(id);
		if (!optionalUser.isPresent()) {
			initOnRun();
			return userSnapshotStore.get(id);
		}
		UserEntity user = optionalUser.get();
		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setUserName(user.getUserName());
		userDTO.setTaxId(user.getTaxId());
		userDTO.setFailureReason(user.getFailureReason());
		return userDTO;
	}

	public void initOnRun() {
		if (userSnapshotStore != null) {
			return;
		}
		userSnapshotStore = streamsBuilderFactoryBean.getKafkaStreams().store(KafkaConstants.USER_STORE,
				QueryableStoreTypes.keyValueStore());
	}
}
