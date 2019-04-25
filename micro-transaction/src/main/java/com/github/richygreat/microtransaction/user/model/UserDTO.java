package com.github.richygreat.microtransaction.user.model;

import lombok.Data;

@Data
public class UserDTO {
	private String id;
	private String taxId;
	private String failureReason;
}
