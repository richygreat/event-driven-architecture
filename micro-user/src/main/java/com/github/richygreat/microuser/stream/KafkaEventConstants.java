package com.github.richygreat.microuser.stream;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KafkaEventConstants {
	public static final String USER_PENDING_CREATION = "PENDING_CREATION";
	public static final String USER_CREATION_FAILED = "CREATION_FAILED";
	public static final String USER_CREATED = "CREATED";

	public static final String HEADER_PREFIX = "headers['event'] == '";
	public static final String HEADER_SUFFIX = "'";
	public static final String USER_PENDING_CREATION_HEADER = HEADER_PREFIX + USER_PENDING_CREATION + HEADER_SUFFIX;
}
