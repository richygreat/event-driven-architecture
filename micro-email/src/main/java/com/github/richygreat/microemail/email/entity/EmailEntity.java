package com.github.richygreat.microemail.email.entity;

import com.github.richygreat.microemail.email.model.EmailType;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class EmailEntity {
    @Id
    private String id;
    private String email;
    private EmailType type;
}
