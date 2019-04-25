package com.github.richygreat.microemail.user.service;

import javax.transaction.Transactional;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.github.richygreat.microemail.stream.KafkaChannel;
import com.github.richygreat.microemail.stream.KafkaEventConstants;
import com.github.richygreat.microemail.user.entity.UserEntity;
import com.github.richygreat.microemail.user.model.UserDTO;
import com.github.richygreat.microemail.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	@Transactional
	@StreamListener(value = KafkaChannel.USER_SINK_CHANNEL, condition = KafkaEventConstants.USER_CREATED_HEADER)
	public void handleUserCreated(@Payload UserDTO userDTO, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
		log.info("handleUserCreated: Entering: userDTO: {} partition: {}", userDTO, partition);
		UserEntity user = new UserEntity();
		user.setId(userDTO.getId());
		user.setUserName(userDTO.getUserName());
		userRepository.save(user);
	}
}
