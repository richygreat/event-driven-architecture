package com.github.richygreat.microemail.email.repository;

import com.github.richygreat.microemail.email.entity.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<EmailEntity, String> {
}
