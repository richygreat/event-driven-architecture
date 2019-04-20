package com.github.richygreat.microbankbff.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.richygreat.microbankbff.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {
	Optional<UserEntity> findByUserName(String userName);
}
