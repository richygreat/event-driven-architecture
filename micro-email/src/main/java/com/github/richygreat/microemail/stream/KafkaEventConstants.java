package com.github.richygreat.microemail.stream;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KafkaEventConstants {
    public static final String VERIFICATION_EMAIL_SENT = "VERIFICATION_EMAIL_SENT";
    public static final String VERIFICATION_EMAIL_LINK_CLICKED = "VERIFICATION_EMAIL_LINK_CLICKED";
    private static final String USER_CREATED = "CREATED";
    private static final String HEADER_PREFIX = "headers['event'] == '";
    private static final String HEADER_SUFFIX = "'";
    public static final String USER_CREATED_HEADER = HEADER_PREFIX + USER_CREATED + HEADER_SUFFIX;
}
