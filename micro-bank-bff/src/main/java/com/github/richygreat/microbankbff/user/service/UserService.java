package com.github.richygreat.microbankbff.user.service;

import java.util.UUID;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.github.richygreat.microbankbff.stream.KafkaChannel;
import com.github.richygreat.microbankbff.stream.KafkaConstants;
import com.github.richygreat.microbankbff.stream.Source;
import com.github.richygreat.microbankbff.user.model.UserDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	private final Source source;

	public void createUser(UserDTO userDTO) {
		userDTO.setId(UUID.randomUUID().toString());
		source.user().send(
				MessageBuilder.withPayload(userDTO).setHeader(KafkaConstants.PARTITION_KEY, userDTO.getId()).build());
	}

	@StreamListener(KafkaChannel.USER_SINK_CHANNEL)
	public void handleUserCreated(@Payload UserDTO userDTO) {
		log.info("handleUserCreated: Entering userDTO: {}", userDTO);
	}
}
