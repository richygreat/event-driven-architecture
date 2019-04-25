package com.github.richygreat.microemail.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.richygreat.microemail.transaction.entity.TransactionEntity;

public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {
}
