package io.pismo.creditaccount.service.impl.factory;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.pismo.creditaccount.model.enums.TransactionType;
import io.pismo.creditaccount.rest.exception.BusinessException;
import io.pismo.creditaccount.service.impl.strategy.TransactionProcessor;

@Component
public class TransactionFactory {

	private Map<TransactionType, TransactionProcessor> strategies;

	@Autowired
	public TransactionFactory(Set<TransactionProcessor> strategySet) {
		createTransactionProcessor(strategySet);
	}

	public TransactionProcessor findTransactionProcessor(TransactionType transactionType) {
		if (transactionType == null) {
			throw new BusinessException("The transaction type was not informed.");
		}
		return strategies.get(transactionType);
	}

	private void createTransactionProcessor(Set<TransactionProcessor> strategySet) {
		this.strategies = new EnumMap<>(TransactionType.class);
		strategySet.forEach(strategy -> strategies.put(strategy.getTransactionType(), strategy));
	}

}