package com.github.richygreat.microbankbff.stream;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KafkaConstants {
	public static String PARTITION_KEY = "partitionKey";
	public static String EVENT = "event";

	public static String USER_STORE = "user-snapshot";
	public static String TRANSACTION_STORE = "transaction-snapshot";
}
