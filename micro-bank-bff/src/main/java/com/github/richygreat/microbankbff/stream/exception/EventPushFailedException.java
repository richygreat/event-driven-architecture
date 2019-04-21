package com.github.richygreat.microbankbff.stream.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Event producer failed to send message")
public class EventPushFailedException extends RuntimeException {
	private static final long serialVersionUID = 6810876155783296907L;
}
