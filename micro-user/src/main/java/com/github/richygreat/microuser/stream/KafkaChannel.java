package com.github.richygreat.microuser.stream;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KafkaChannel {
    public static final String USER_SINK_CHANNEL = "user-sink";
    public static final String EMAIL_SINK_CHANNEL = "email-sink";
    static final String USER_SOURCE_CHANNEL = "user-source";
}
