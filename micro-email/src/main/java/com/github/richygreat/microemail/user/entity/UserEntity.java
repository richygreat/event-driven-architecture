package com.github.richygreat.microemail.user.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class UserEntity {
    @Id
    private String id;
    private String email;
}
