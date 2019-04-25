package com.github.richygreat.microuser.stream;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KafkaEventConstants {
	public static final String USER_CREATION_REQUESTED = "CREATION_REQUESTED";
	public static final String USER_CREATED = "CREATED";

	public static final String HEADER_PREFIX = "headers['event'] == '";
	public static final String HEADER_SUFFIX = "'";
	public static final String USER_CREATION_REQUESTED_HEADER = HEADER_PREFIX + USER_CREATION_REQUESTED + HEADER_SUFFIX;
}
