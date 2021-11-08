package io.pismo.creditaccount.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.pismo.creditaccount.data.vo.form.TransactionFormVO;
import io.pismo.creditaccount.model.entity.Transaction;
import io.pismo.creditaccount.model.repository.TransactionRepository;
import io.pismo.creditaccount.rest.exception.BusinessException;
import io.pismo.creditaccount.service.TransactionService;
import io.pismo.creditaccount.service.impl.factory.TransactionFactory;
import io.pismo.creditaccount.service.impl.strategy.TransactionProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

	private TransactionRepository transactionRepository;
	private TransactionFactory transactionFactory;

	@Override
	@Transactional(readOnly = true)
	public Optional<Transaction> findById(Long id) {
		return transactionRepository.findById(id);
	}

	@Override
	@Transactional
	public void save(TransactionFormVO form) throws BusinessException {
		try {
			TransactionProcessor tp = transactionFactory.findTransactionProcessor(form.getType());
			tp.process(form);

		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new BusinessException("sorry! could not process this transaction.");
		}
	}

}
