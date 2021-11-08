package io.pismo.creditaccount.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.pismo.creditaccount.model.entity.Account;
import io.pismo.creditaccount.model.entity.Invoice;
import io.pismo.creditaccount.model.entity.Transaction;
import io.pismo.creditaccount.model.enums.InvoiceStatus;
import io.pismo.creditaccount.model.enums.TransactionType;
import io.pismo.creditaccount.model.repository.InvoiceRepository;
import io.pismo.creditaccount.model.repository.TransactionRepository;
import io.pismo.creditaccount.rest.exception.BusinessException;
import io.pismo.creditaccount.service.InvoiceService;
import io.pismo.creditaccount.utils.RandomUtil;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

	private static final String CREDITO_FATURA_ANTERIOR = "CRÉDITO FATURA ANTERIOR";

	private InvoiceRepository invoiceRepository;
	private TransactionRepository transactionRepository;

	@Override
	@Transactional(readOnly = true)
	public Optional<Invoice> findById(Long id) {
		return invoiceRepository.findById(id);
	}

	@Override
	@Transactional
	public Invoice processInvoiceByAccount(Account account) {
		List<Transaction> transactions = transactionRepository.listPendingTransactionsByAccountID(account.getId());
		if (transactions.isEmpty()) {
			throw new BusinessException("No transactions found.");
		}

		BigDecimal lancamentosFatura = transactions.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
		InvoiceStatus statusFatura;
		BigDecimal valorFatura;

		if (lancamentosFatura.compareTo(BigDecimal.ZERO) == 0) {
			statusFatura = InvoiceStatus.PAGO_TOTALMENTE;
			valorFatura = BigDecimal.ZERO;
		} else if (lancamentosFatura.compareTo(BigDecimal.ZERO) < 0) {
			statusFatura = InvoiceStatus.AGUARDANDO_PAGAMENTO;
			valorFatura = lancamentosFatura.multiply(BigDecimal.valueOf(-1));
		} else {
			statusFatura = InvoiceStatus.PAGO_TOTALMENTE;
			valorFatura = BigDecimal.ZERO;
			Transaction transaction = Transaction.builder()
					.account(account)
					.type(TransactionType.PAGAMENTO)
					.description(CREDITO_FATURA_ANTERIOR)
					.amount(lancamentosFatura)
					.eventDate(LocalDateTime.now())
					.build();
			transactionRepository.save(transaction);
		}

		Invoice invoice = Invoice.builder()
			.account(account)
			.status(statusFatura)
			.invoiceNumber(RandomUtil.generateRandomNumber())
			.paymentDueDate(LocalDate.now().plusDays(10))
			.paymentDue(valorFatura)
			.transactions(transactions)
			.build();

		return invoiceRepository.save(invoice);
	}
	
}
