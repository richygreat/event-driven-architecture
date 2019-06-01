package com.github.richygreat.microuser.user.model;

import lombok.Data;

@Data
public class UserDTO {
    private String id;
    private String email;
    private String taxId;
    private UserStatus status;
}
