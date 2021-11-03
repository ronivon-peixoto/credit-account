package io.pismo.creditaccount.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.pismo.creditaccount.model.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	BigDecimal calculateUsedCreditLimit(Long accountId);

	BigDecimal calculateUsedWithdrawalLimit(Long accountId);

	List<Transaction> listPendingTransactionsByAccountID(Long accountId);

}
