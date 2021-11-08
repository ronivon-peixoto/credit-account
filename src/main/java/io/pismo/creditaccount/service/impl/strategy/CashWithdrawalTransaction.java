package io.pismo.creditaccount.service.impl.strategy;

import static io.pismo.creditaccount.utils.StringUtil.concat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CashWithdrawalTransaction implements TransactionProcessor {

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
		BigDecimal cashWithdrawalLimitUsed = transactionRepository.calculateUsedWithdrawalLimit(account.getId());
		if (account.getCreditLimit().subtract(creditLimitUsed).compareTo(form.getAmount()) < 0) {
			throw new BusinessException(LIMITE_CREDITO_EXCEDIDO);
		} else if (account.getWithdrawalLimit().subtract(cashWithdrawalLimitUsed).compareTo(form.getAmount()) < 0) {
			throw new BusinessException(LIMITE_SAQUE_EXCEDIDO);
		}
		Transaction transaction = Transaction.builder()
				.account(account)
				.type(form.getType())
				.description(concat("", form.getDescription()))
				.amount(form.getAmount().negate())
				.eventDate(LocalDateTime.now())
				.build();

		transactionRepository.save(transaction);
	}

	@Override
	public TransactionType getTransactionType() {
		return TransactionType.SAQUE;
	}

}
