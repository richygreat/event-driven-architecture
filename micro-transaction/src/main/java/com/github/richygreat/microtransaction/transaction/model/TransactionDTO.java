package com.github.richygreat.microtransaction.transaction.model;

import lombok.Data;

@Data
public class TransactionDTO {
	private String id;
	private String userId;
	private Double amount;
	private String failureReason;
}
