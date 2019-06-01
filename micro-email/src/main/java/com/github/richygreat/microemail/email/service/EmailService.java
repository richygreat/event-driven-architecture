package com.github.richygreat.microemail.email.service;

import com.github.richygreat.microemail.email.entity.EmailEntity;
import com.github.richygreat.microemail.email.model.EmailDTO;
import com.github.richygreat.microemail.email.model.EmailType;
import com.github.richygreat.microemail.email.repository.EmailRepository;
import com.github.richygreat.microemail.stream.KafkaEventConstants;
import com.github.richygreat.microemail.stream.KafkaMessageUtility;
import com.github.richygreat.microemail.stream.Source;
import com.github.richygreat.microemail.stream.exception.EventPushFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final Source source;
    private final EmailRepository emailRepository;

    public void sendVerificationMail(EmailDTO emailDTO) {
        EmailEntity email = new EmailEntity();
        email.setId(emailDTO.getId());
        email.setEmail(emailDTO.getEmail());
        email.setType(EmailType.VERIFY_EMAIL);
        emailRepository.save(email);

        log.info("sendVerificationMail: link: http://localhost:8083/emails/{}/click", emailDTO.getId());

        boolean sent = source.emailProducer().send(KafkaMessageUtility.createMessage(emailDTO,
                KafkaEventConstants.VERIFICATION_EMAIL_SENT, emailDTO.getId(), emailDTO.getEmail()));
        log.info("sendVerificationMail: Exiting emailDTO: {} sent: {}", emailDTO.getId(), sent);
        if (!sent) {
            throw new EventPushFailedException();
        }
    }

    public void handleLinkClick(String id) {
        emailRepository.findById(id).ifPresent(email -> {
            if (!EmailType.VERIFY_EMAIL.equals(email.getType())) {
                return;
            }

            EmailDTO emailDTO = new EmailDTO();
            emailDTO.setId(email.getId());
            emailDTO.setEmail(email.getEmail());

            boolean sent = source.emailProducer().send(KafkaMessageUtility.createMessage(emailDTO,
                    KafkaEventConstants.VERIFICATION_EMAIL_LINK_CLICKED, emailDTO.getId(), emailDTO.getEmail()));
            log.info("handleLinkClick: Exiting emailDTO: {} sent: {}", emailDTO.getId(), sent);
            if (!sent) {
                throw new EventPushFailedException();
            }
        });
    }
}
