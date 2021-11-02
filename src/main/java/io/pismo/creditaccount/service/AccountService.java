package io.pismo.creditaccount.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.pismo.creditaccount.data.vo.form.AccountFormVO;
import io.pismo.creditaccount.model.entity.Account;
import io.pismo.creditaccount.model.entity.Card;
import io.pismo.creditaccount.model.repository.AccountRepository;
import io.pismo.creditaccount.model.repository.CardRepository;
import io.pismo.creditaccount.utils.RandomUtil;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountService {

	private AccountRepository accountRepository;
	private CardRepository cardRepository;

	@Transactional(readOnly = true)
	public Optional<Account> findById(Long id) {
		return accountRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public List<Account> listByInvoiceClosingDay(Integer dayOfMonth) {
		return accountRepository.listByInvoiceClosingDay(dayOfMonth);
	}

	@Transactional
	public Account save(AccountFormVO form) {
		Card card = Card.builder()
				.cardNumber(RandomUtil.generateRandomNumber())
				.isActive(Boolean.TRUE).build();

		Account account = Account.builder()
				.docNumber(form.getDocNumber())
				.creditLimit(form.getCreditLimit())
				.withdrawalLimit(form.getWithdrawalLimit())
				.invoiceClosingDay(form.getInvoiceClosingDay())
				.isActive(Boolean.TRUE)
				.card(cardRepository.save(card))
				.build();

		return accountRepository.save(account);
	}

}
