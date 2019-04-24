package com.github.richygreat.microbankbff.transaction.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class TransactionEntity {
	@Id
	private String id;
	private String userId;
	private Double amount;
	private String failureReason;
}
