package com.github.richygreat.microemail.stream;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KafkaChannel {
    public static final String USER_SINK_CHANNEL = "user-sink";

    static final String EMAIL_SOURCE_CHANNEL = "email-source";
}
