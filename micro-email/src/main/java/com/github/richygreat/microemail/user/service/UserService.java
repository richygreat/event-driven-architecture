package com.github.richygreat.microemail.user.service;

import com.github.richygreat.microemail.email.model.EmailDTO;
import com.github.richygreat.microemail.email.service.EmailService;
import com.github.richygreat.microemail.stream.KafkaChannel;
import com.github.richygreat.microemail.stream.KafkaEventConstants;
import com.github.richygreat.microemail.user.entity.UserEntity;
import com.github.richygreat.microemail.user.model.UserDTO;
import com.github.richygreat.microemail.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final EmailService emailService;
    private final UserRepository userRepository;

    @Transactional
    @StreamListener(value = KafkaChannel.USER_SINK_CHANNEL, condition = KafkaEventConstants.USER_CREATED_HEADER)
    public void handleUserCreated(@Payload UserDTO userDTO, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        log.info("handleUserCreated: Entering: userDTO: {} partition: {}", userDTO, partition);

        UserEntity user = new UserEntity();
        user.setId(userDTO.getId());
        user.setEmail(userDTO.getEmail());
        userRepository.save(user);

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setId(UUID.randomUUID().toString());//send this id in link
        emailDTO.setEmail(userDTO.getEmail());
        emailService.sendVerificationMail(emailDTO);
    }
}
