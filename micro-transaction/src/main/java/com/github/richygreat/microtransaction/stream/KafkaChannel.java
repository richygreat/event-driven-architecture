package com.github.richygreat.microtransaction.stream;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KafkaChannel {
	public static final String USER_SINK_CHANNEL = "user-sink";
	public static final String TRANSACTION_SINK_CHANNEL = "transaction-sink";
	public static final String TRANSACTION_SOURCE_CHANNEL = "transaction-source";
}
