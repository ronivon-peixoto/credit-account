package io.pismo.creditaccount.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.pismo.creditaccount.model.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
