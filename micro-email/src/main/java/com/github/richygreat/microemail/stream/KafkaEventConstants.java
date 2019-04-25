package com.github.richygreat.microemail.stream;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KafkaEventConstants {
	public static final String USER_CREATED = "CREATED";
	public static final String TRANSACTION_CREATED = "CREATED";

	public static final String HEADER_PREFIX = "headers['event'] == '";
	public static final String HEADER_SUFFIX = "'";
	public static final String USER_CREATED_HEADER = HEADER_PREFIX + USER_CREATED + HEADER_SUFFIX;
	public static final String TRANSACTION_CREATED_HEADER = HEADER_PREFIX + TRANSACTION_CREATED + HEADER_SUFFIX;
}
