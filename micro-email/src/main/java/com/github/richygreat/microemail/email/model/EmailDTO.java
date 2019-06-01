package com.github.richygreat.microemail.email.model;

import lombok.Data;

@Data
public class EmailDTO {
    private String id;
    private String email;
    private EmailType type;
}
