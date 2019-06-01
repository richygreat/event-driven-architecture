package com.github.richygreat.microuser.user.entity;

import com.github.richygreat.microuser.user.model.UserStatus;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class UserEntity {
    @Id
    private String id;
    private String email;
    private String taxId;
    private UserStatus status;
}
