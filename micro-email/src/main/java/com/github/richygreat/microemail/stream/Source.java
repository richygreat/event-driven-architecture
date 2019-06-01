package com.github.richygreat.microemail.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface Source {
    @Output(KafkaChannel.EMAIL_SOURCE_CHANNEL)
    MessageChannel emailProducer();
}
