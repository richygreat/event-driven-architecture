package com.github.richygreat.microuser.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface Source {
	@Output(KafkaChannel.USER_SOURCE_CHANNEL)
	MessageChannel userProducer();
}
