package com.github.richygreat.microuser.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface Sink {
    @Input(KafkaChannel.USER_SINK_CHANNEL)
    SubscribableChannel userConsumer();

    @Input(KafkaChannel.EMAIL_SINK_CHANNEL)
    SubscribableChannel emailConsumer();
}
