package com.github.richygreat.microtransaction.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.richygreat.microtransaction.transaction.entity.TransactionEntity;

public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {
}
