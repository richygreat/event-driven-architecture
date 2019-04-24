package com.github.richygreat.microbankbff.stream;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KafkaEventConstants {
	public static final String USER_CREATION_REQUESTED = "CREATION_REQUESTED";
	public static final String USER_CREATION_FAILED = "CREATION_FAILED";
	public static final String USER_CREATED = "CREATED";

	public static final String TRANSACTION_CREATION_REQUESTED = "CREATION_REQUESTED";
	public static final String TRANSACTION_CREATION_FAILED = "CREATION_FAILED";
	public static final String TRANSACTION_CREATED = "CREATED";

	public static final String HEADER_PREFIX = "headers['event'] == '";
	public static final String HEADER_SUFFIX = "'";

	public static final String USER_CREATION_FAILED_HEADER = HEADER_PREFIX + USER_CREATION_FAILED + HEADER_SUFFIX;
	public static final String USER_CREATED_HEADER = HEADER_PREFIX + USER_CREATED + HEADER_SUFFIX;

	public static final String TRANSACTION_CREATION_FAILED_HEADER = HEADER_PREFIX + TRANSACTION_CREATION_FAILED
			+ HEADER_SUFFIX;
	public static final String TRANSACTION_CREATED_HEADER = HEADER_PREFIX + TRANSACTION_CREATED + HEADER_SUFFIX;
}
