package io.pismo.creditaccount.service.impl.strategy;

import static io.pismo.creditaccount.utils.StringUtil.concat;
import static io.pismo.creditaccount.utils.StringUtil.concatItemInQtde;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.pismo.creditaccount.data.vo.form.TransactionFormVO;
import io.pismo.creditaccount.model.entity.Account;
import io.pismo.creditaccount.model.entity.Transaction;
import io.pismo.creditaccount.model.enums.TransactionType;
import io.pismo.creditaccount.model.repository.AccountRepository;
import io.pismo.creditaccount.model.repository.TransactionRepository;
import io.pismo.creditaccount.rest.exception.BusinessException;
import io.pismo.creditaccount.utils.CurrencyUtil;
import io.pismo.creditaccount.utils.DateUtil;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PurchaseInInstallmentsTransaction implements TransactionProcessor {

	private TransactionRepository transactionRepository;
	private AccountRepository accountRepository;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void process(TransactionFormVO form) {
		Account account = accountRepository.findAccountByCardNumber(form.getCardNumber())
				.orElseThrow(() -> new BusinessException(NUM_CARTAO_INVALIDO));

		if (Boolean.FALSE.equals(account.getIsActive()) || Boolean.FALSE.equals(account.getCard().getIsActive())) {
			throw new BusinessException(CONTA_INATIVA);
		}
		BigDecimal creditLimitUsed = transactionRepository.calculateUsedCreditLimit(account.getId());
		if (account.getCreditLimit().subtract(creditLimitUsed).compareTo(form.getAmount()) < 0) {
			throw new BusinessException(LIMITE_CREDITO_EXCEDIDO);
		}
		List<Transaction> transactions = new ArrayList<>();
		BigDecimal[] amounts = CurrencyUtil.generateInstallmentAmounts(form.getAmount(), form.getInstallments());
		LocalDateTime[] dates = DateUtil.generateDueDates(LocalDateTime.now(), form.getInstallments());

		IntStream.range(0, amounts.length).forEach(idx -> 
			transactions.add(Transaction.builder()
				.account(account)
				.type(form.getType())
				.description(concat("", form.getDescription(), concatItemInQtde(idx + 1, form.getInstallments())))
				.amount(amounts[idx].negate())
				.eventDate(dates[idx])
				.build()));

		transactionRepository.saveAll(transactions);
	}

	@Override
	public TransactionType getTransactionType() {
		return TransactionType.COMPRA_PARCELADA;
	}

}
