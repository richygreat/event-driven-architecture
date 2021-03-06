package com.github.richygreat.microemail.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface Sink {
    @Input(KafkaChannel.USER_SINK_CHANNEL)
    SubscribableChannel userConsumer();
}
