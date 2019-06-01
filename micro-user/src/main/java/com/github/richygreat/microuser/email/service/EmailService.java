package com.github.richygreat.microuser.email.service;

import com.github.richygreat.microuser.email.model.EmailDTO;
import com.github.richygreat.microuser.stream.KafkaChannel;
import com.github.richygreat.microuser.stream.KafkaEventConstants;
import com.github.richygreat.microuser.user.model.UserStatus;
import com.github.richygreat.microuser.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final UserService userService;

    @Transactional
    @StreamListener(value = KafkaChannel.EMAIL_SINK_CHANNEL, condition = KafkaEventConstants.VERIFICATION_EMAIL_SENT_HEADER)
    public void handleVerificationEmailSent(@Payload EmailDTO emailDTO, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        log.info("handleVerificationEmailSent: Entering: emailDTO: {} partition: {}", emailDTO, partition);
        userService.updateUserStatus(emailDTO.getEmail(), UserStatus.PENDING_VERIFICATION);
    }

    @Transactional
    @StreamListener(value = KafkaChannel.EMAIL_SINK_CHANNEL, condition = KafkaEventConstants.VERIFICATION_EMAIL_LINK_CLICKED_HEADER)
    public void handleVerificationEmailLinkClicked(@Payload EmailDTO emailDTO, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        log.info("handleVerificationEmailLinkClicked: Entering: emailDTO: {} partition: {}", emailDTO, partition);
        userService.updateUserStatus(emailDTO.getEmail(), UserStatus.ACTIVE);
    }
}
