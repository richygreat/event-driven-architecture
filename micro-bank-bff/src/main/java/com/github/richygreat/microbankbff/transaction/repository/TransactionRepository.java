package com.github.richygreat.microbankbff.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.richygreat.microbankbff.transaction.entity.TransactionEntity;

public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {
}
