package com.github.richygreat.microtransaction.user.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class UserEntity {
	@Id
	private String id;
	private String userName;
	private String taxId;
	private Double balance = 0.0;
}
