package io.pismo.creditaccount.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import io.pismo.creditaccount.data.vo.form.AccountFormVO;
import io.pismo.creditaccount.model.entity.Account;
import io.pismo.creditaccount.model.entity.Card;
import io.pismo.creditaccount.model.repository.AccountRepository;

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceImplTest {

	private static final Long CARD_ID = 1L;
	private static final String CARD_NUMBER = "0123456789101112";
	private static final Boolean CARD_IS_ACTIVE = Boolean.TRUE;

	private static final Long ACCOUNT_ID = 1L;
	private static final String ACCOUNT_DOC_NUMBER = "0123456789101112";
	private static final BigDecimal CREDIT_LIMIT = BigDecimal.valueOf(10000.00);
	private static final BigDecimal WITHDRAWAL_LIMIT = BigDecimal.valueOf(1000.00);
	private static final Integer INVOICE_CLOSING_DAY = 15;
	private static final Boolean ACCOUNT_IS_ACTIVE = Boolean.TRUE;

	@MockBean
	private AccountRepository accountRepository;

	@Autowired
	private AccountServiceImpl accountService;

	@Test
	void testFindById__NotFound() {
		doReturn(Optional.empty()).when(accountRepository).findById(100L);

		Optional<Account> accountOpt = accountService.findById(100L);

		assertNotNull(accountOpt);
		assertFalse(accountOpt.isPresent());
	}

	@Test
	void testFindById() {
		Card cardMock = Card.builder()
				.id(CARD_ID)
				.cardNumber(CARD_NUMBER)
				.isActive(CARD_IS_ACTIVE)
				.build();
		Account accountMock = Account.builder()
				.id(ACCOUNT_ID)
				.docNumber(ACCOUNT_DOC_NUMBER)
				.creditLimit(CREDIT_LIMIT)
				.withdrawalLimit(WITHDRAWAL_LIMIT)
				.invoiceClosingDay(INVOICE_CLOSING_DAY)
				.card(cardMock)
				.isActive(ACCOUNT_IS_ACTIVE)
				.build();

		doReturn(Optional.of(accountMock)).when(accountRepository).findById(ACCOUNT_ID);

		Optional<Account> accountOpt = accountService.findById(ACCOUNT_ID);

		assertNotNull(accountOpt);
		assertTrue(accountOpt.isPresent());

		Account account = accountOpt.get();

		assertEquals(ACCOUNT_ID, account.getId());
		assertEquals(ACCOUNT_DOC_NUMBER, account.getDocNumber());
		assertEquals(CREDIT_LIMIT, account.getCreditLimit());
		assertEquals(WITHDRAWAL_LIMIT, account.getWithdrawalLimit());
		assertEquals(INVOICE_CLOSING_DAY, account.getInvoiceClosingDay());
		assertEquals(ACCOUNT_IS_ACTIVE, account.getIsActive());

		assertNotNull(account.getCard());

		Card card = account.getCard();

		assertEquals(CARD_ID, card.getId());
		assertEquals(CARD_NUMBER, card.getCardNumber());
		assertEquals(CARD_IS_ACTIVE, card.getIsActive());
	}

	@Test
	void testListByInvoiceClosingDay__EmptyResult() {
		doReturn(Collections.emptyList()).when(accountRepository).listByInvoiceClosingDay(INVOICE_CLOSING_DAY);

		List<Account> result = accountService.listByInvoiceClosingDay(INVOICE_CLOSING_DAY);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	@Test
	void testListByInvoiceClosingDay() {
		Account accountMock = Account.builder()
				.id(ACCOUNT_ID)
				.docNumber(ACCOUNT_DOC_NUMBER)
				.creditLimit(CREDIT_LIMIT)
				.withdrawalLimit(WITHDRAWAL_LIMIT)
				.invoiceClosingDay(INVOICE_CLOSING_DAY)
				.isActive(ACCOUNT_IS_ACTIVE)
				.build();

		doReturn(List.of(accountMock)).when(accountRepository).listByInvoiceClosingDay(INVOICE_CLOSING_DAY);

		List<Account> result = accountService.listByInvoiceClosingDay(INVOICE_CLOSING_DAY);

		assertNotNull(result);
		assertEquals(1, result.size());

		Account account = result.get(0);

		assertEquals(ACCOUNT_ID, account.getId());
		assertEquals(ACCOUNT_DOC_NUMBER, account.getDocNumber());
		assertEquals(CREDIT_LIMIT, account.getCreditLimit());
		assertEquals(WITHDRAWAL_LIMIT, account.getWithdrawalLimit());
		assertEquals(INVOICE_CLOSING_DAY, account.getInvoiceClosingDay());
		assertEquals(ACCOUNT_IS_ACTIVE, account.getIsActive());
	}

	@Test
	void testSave() {
		Card cardMock = Card.builder()
				.id(CARD_ID)
				.cardNumber(CARD_NUMBER)
				.isActive(CARD_IS_ACTIVE)
				.build();

		Account accountMock = Account.builder()
				.id(ACCOUNT_ID)
				.docNumber(ACCOUNT_DOC_NUMBER)
				.creditLimit(CREDIT_LIMIT)
				.withdrawalLimit(WITHDRAWAL_LIMIT)
				.invoiceClosingDay(INVOICE_CLOSING_DAY)
				.card(cardMock)
				.isActive(ACCOUNT_IS_ACTIVE)
				.build();

		AccountFormVO AccountForm = AccountFormVO.builder()
			.docNumber(ACCOUNT_DOC_NUMBER)
			.creditLimit(CREDIT_LIMIT)
			.withdrawalLimit(WITHDRAWAL_LIMIT)
			.invoiceClosingDay(INVOICE_CLOSING_DAY)
			.build();

		doReturn(accountMock).when(accountRepository).save(any());

		Account account = accountService.save(AccountForm);

		assertNotNull(account);

		assertEquals(ACCOUNT_ID, account.getId());
		assertEquals(ACCOUNT_DOC_NUMBER, account.getDocNumber());
		assertEquals(CREDIT_LIMIT, account.getCreditLimit());
		assertEquals(WITHDRAWAL_LIMIT, account.getWithdrawalLimit());
		assertEquals(INVOICE_CLOSING_DAY, account.getInvoiceClosingDay());
		assertEquals(ACCOUNT_IS_ACTIVE, account.getIsActive());

		assertNotNull(account.getCard());

		Card card = account.getCard();

		assertEquals(CARD_ID, card.getId());
		assertEquals(CARD_NUMBER, card.getCardNumber());
		assertEquals(CARD_IS_ACTIVE, card.getIsActive());
	}

}
