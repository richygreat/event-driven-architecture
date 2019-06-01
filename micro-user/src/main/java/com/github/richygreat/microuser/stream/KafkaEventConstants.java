package com.github.richygreat.microuser.stream;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KafkaEventConstants {
    public static final String USER_CREATION_REQUESTED = "CREATION_REQUESTED";
    public static final String USER_CREATED = "CREATED";

    private static final String HEADER_PREFIX = "headers['event'] == '";
    private static final String HEADER_SUFFIX = "'";
    public static final String USER_CREATION_REQUESTED_HEADER = HEADER_PREFIX + USER_CREATION_REQUESTED + HEADER_SUFFIX;

    private static final String VERIFICATION_EMAIL_SENT = "VERIFICATION_EMAIL_SENT";
    public static final String VERIFICATION_EMAIL_SENT_HEADER = HEADER_PREFIX + VERIFICATION_EMAIL_SENT + HEADER_SUFFIX;
    private static final String VERIFICATION_EMAIL_LINK_CLICKED = "VERIFICATION_EMAIL_LINK_CLICKED";
    public static final String VERIFICATION_EMAIL_LINK_CLICKED_HEADER = HEADER_PREFIX + VERIFICATION_EMAIL_LINK_CLICKED + HEADER_SUFFIX;
}
