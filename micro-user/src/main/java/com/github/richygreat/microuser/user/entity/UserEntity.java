package com.github.richygreat.microuser.user.entity;

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
}
