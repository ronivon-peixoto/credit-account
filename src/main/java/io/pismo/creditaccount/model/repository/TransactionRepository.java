package io.pismo.creditaccount.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.pismo.creditaccount.model.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	BigDecimal calcularLimiteCreditoUtilizado(Long accountId);

	BigDecimal calcularLimiteSaqueUtilizado(Long accountId);

	List<Transaction> listPendingTransactionsByAccountID(Long accountId);

}
