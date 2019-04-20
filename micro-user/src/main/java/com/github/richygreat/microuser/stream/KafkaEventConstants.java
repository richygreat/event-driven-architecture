package com.github.richygreat.microuser.stream;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KafkaEventConstants {
	public static final String USER_PENDING_CREATION = "PENDING_CREATION";
	public static final String USER_CREATION_FAILED = "CREATION_FAILED";
	public static final String USER_CREATED = "CREATED";
	public static final String CONDITION_USER_PENDING_CREATION = "headers['event'] == '" + USER_PENDING_CREATION + "'";
}
