package com.github.richygreat.microemail.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.richygreat.microemail.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {
}
