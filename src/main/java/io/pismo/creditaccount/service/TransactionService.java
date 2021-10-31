package io.pismo.creditaccount.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import io.pismo.creditaccount.data.vo.form.TransactionFormVO;
import io.pismo.creditaccount.model.entity.Transaction;
import io.pismo.creditaccount.model.repository.TransactionRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TransactionService {

	private TransactionRepository transactionRepository;

	public Optional<Transaction> findById(Long id) {
		return transactionRepository.findById(id);
	}

	public Transaction save(TransactionFormVO form) {
		// TODO Auto-generated method stub
		return null;
	}

}
