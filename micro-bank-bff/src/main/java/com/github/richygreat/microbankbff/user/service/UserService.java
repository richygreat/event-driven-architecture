package com.github.richygreat.microbankbff.user.service;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.github.richygreat.microbankbff.stream.KafkaChannel;
import com.github.richygreat.microbankbff.stream.KafkaEventConstants;
import com.github.richygreat.microbankbff.stream.KafkaMessageUtility;
import com.github.richygreat.microbankbff.stream.Source;
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

	@Transactional
	public void createUser(UserDTO userDTO) {
		Optional<UserEntity> optionalUser = userRepository.findByUserName(userDTO.getUserName());
		if (optionalUser.isPresent()) {
			throw new UserAlreadyExistsException();
		}
		userDTO.setId(UUID.randomUUID().toString());
		source.userProducer().send(KafkaMessageUtility.createMessage(userDTO, userDTO.getUserName(),
				KafkaEventConstants.USER_PENDING_CREATION));
	}

	@Transactional
	@StreamListener(value = KafkaChannel.USER_SINK_CHANNEL, condition = KafkaEventConstants.CONDITION_USER_PENDING_CREATION)
	public void handleUserPendingCreation(@Payload UserDTO userDTO,
			@Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
		log.info("handleUserPendingCreation: Entering: userDTO: {} partition: {}", userDTO, partition);
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
}
