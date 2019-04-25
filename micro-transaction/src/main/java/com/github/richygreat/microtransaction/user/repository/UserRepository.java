package com.github.richygreat.microtransaction.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.richygreat.microtransaction.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {
}
