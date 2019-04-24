package com.github.richygreat.microtransaction.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface Source {
	@Output(KafkaChannel.TRANSACTION_SOURCE_CHANNEL)
	MessageChannel transactionProducer();
}
