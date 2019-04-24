package com.github.richygreat.microtransaction.stream;

import org.springframework.integration.support.MessageBuilder;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KafkaMessageUtility {
	public <T> Message<T> createMessage(T payload, String event, String key, String partitionKey) {
		return MessageBuilder.withPayload(payload).setHeader(KafkaConstants.EVENT, event)
				.setHeader(KafkaConstants.PARTITION_KEY, partitionKey).setHeader(KafkaHeaders.MESSAGE_KEY, key).build();
	}
}
