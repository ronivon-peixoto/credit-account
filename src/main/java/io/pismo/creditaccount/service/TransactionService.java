package io.pismo.creditaccount.service;

import java.util.Optional;

import io.pismo.creditaccount.data.vo.form.TransactionFormVO;
import io.pismo.creditaccount.model.entity.Transaction;

public interface TransactionService {

	Optional<Transaction> findById(Long id);

	void save(TransactionFormVO form);

}
