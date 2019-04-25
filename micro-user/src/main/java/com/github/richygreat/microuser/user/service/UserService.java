package com.github.richygreat.microuser.user.service;

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

import com.github.richygreat.microuser.stream.KafkaChannel;
import com.github.richygreat.microuser.stream.KafkaConstants;
import com.github.richygreat.microuser.stream.KafkaEventConstants;
import com.github.richygreat.microuser.stream.KafkaMessageUtility;
import com.github.richygreat.microuser.stream.Source;
import com.github.richygreat.microuser.stream.exception.EventPushFailedException;
import com.github.richygreat.microuser.user.entity.UserEntity;
import com.github.richygreat.microuser.user.exception.UserAlreadyExistsException;
import com.github.richygreat.microuser.user.model.UserDTO;
import com.github.richygreat.microuser.user.repository.UserRepository;

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
	@StreamListener(value = KafkaChannel.USER_SINK_CHANNEL, condition = KafkaEventConstants.USER_CREATION_REQUESTED_HEADER)
	public void handleUserCreationRequested(@Payload UserDTO userDTO,
			@Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
		log.info("handleUserCreationRequested: Entering: userDTO: {} partition: {}", userDTO, partition);
		UserEntity user = new UserEntity();
		user.setId(userDTO.getId());
		user.setUserName(userDTO.getUserName());
		user.setTaxId(userDTO.getTaxId());
		userRepository.save(user);
		boolean sent = source.userProducer().send(KafkaMessageUtility.createMessage(userDTO,
				KafkaEventConstants.USER_CREATED, userDTO.getId(), userDTO.getUserName()));
		log.info("handleUserCreationRequested: Exiting userDTO: {} sent: {}", userDTO.getId(), sent);
		if (!sent) {
			throw new EventPushFailedException();
		}
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
