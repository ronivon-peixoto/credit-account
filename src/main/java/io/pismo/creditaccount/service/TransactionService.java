package io.pismo.creditaccount.service;

import static io.pismo.creditaccount.utils.StringUtil.concat;
import static io.pismo.creditaccount.utils.StringUtil.concatItemInQtde;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.pismo.creditaccount.data.vo.form.TransactionFormVO;
import io.pismo.creditaccount.model.entity.Account;
import io.pismo.creditaccount.model.entity.Invoice;
import io.pismo.creditaccount.model.entity.Transaction;
import io.pismo.creditaccount.model.enums.InvoiceStatus;
import io.pismo.creditaccount.model.enums.TransactionType;
import io.pismo.creditaccount.model.repository.AccountRepository;
import io.pismo.creditaccount.model.repository.InvoiceRepository;
import io.pismo.creditaccount.model.repository.TransactionRepository;
import io.pismo.creditaccount.rest.exception.BusinessException;
import io.pismo.creditaccount.utils.CurrencyUtil;
import io.pismo.creditaccount.utils.DateUtil;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TransactionService {

	private static final String PAGAMENTO_TOTAL_FATURA = "PAGAMENTO TOTAL DA FATURA";
	private static final String PAGAMENTO_PARCIAL_FATURA = "PAGAMENTO PARCIAL DA FATURA";

	private static final String NUM_CARTAO_INVALIDO = "O número do cartão é inválido.";
	private static final String NUM_FATURA_INVALIDO = "O número da fatura é inválido.";
	private static final String CONTA_INATIVA = "A conta do usuário está inativa.";
	private static final String FATURA_INDISP_PAGAMENTO = "Fatura indisponível para pagamento.";
	private static final String LIMITE_CREDITO_EXCEDIDO = "Limite de crédito excedido.";
	private static final String LIMITE_SAQUE_EXCEDIDO = "Limite de saque excedido.";

	private static final String CREDITO_FATURA_ANTERIOR = "CRÉDITO FATURA ANTERIOR";
	private static final String SALDO_DEVEDOR_FATURA_ANTERIOR = "SALDO DEVEDOR FATURA ANTERIOR";

	private TransactionRepository transactionRepository;
	private AccountRepository accountRepository;
	private InvoiceRepository invoiceRepository;

	@Transactional(readOnly = true)
	public Optional<Transaction> findById(Long id) {
		return transactionRepository.findById(id);
	}

	@Transactional
	public void save(TransactionFormVO form) {
		if (TransactionType.COMPRA_A_VISTA.equals(form.getType())) {
			processCashPurchase(form);
		} else if (TransactionType.COMPRA_PARCELADA.equals(form.getType())) {
			processPurchaseInInstallments(form);
		} else if (TransactionType.SAQUE.equals(form.getType())) {
			processWithdrawal(form);
		} else if (TransactionType.PAGAMENTO.equals(form.getType())) {
			processPayment(form);
		} else {
			throw new BusinessException("Tipo de transação não suportado.");
		}
	}

	private void processCashPurchase(TransactionFormVO form) {
		Account account = accountRepository.findAccountByCardNumber(form.getCardNumber())
				.orElseThrow(() -> new BusinessException(NUM_CARTAO_INVALIDO));

		if (Boolean.FALSE.equals(account.getIsActive()) || Boolean.FALSE.equals(account.getCard().getIsActive())) {
			throw new BusinessException(CONTA_INATIVA);
		}
		BigDecimal limiteCreditoUtilizado = transactionRepository.calculateUsedCreditLimit(account.getId());
		if (account.getCreditLimit().subtract(limiteCreditoUtilizado).compareTo(form.getAmount()) < 0) {
			throw new BusinessException(LIMITE_CREDITO_EXCEDIDO);
		}
		Transaction transaction = Transaction.builder()
				.account(account)
				.type(form.getType())
				.description(form.getDescription())
				.amount(form.getAmount().negate())
				.eventDate(LocalDateTime.now())
				.build();

		transactionRepository.save(transaction);
	}

	private void processPurchaseInInstallments(TransactionFormVO form) {
		Account account = accountRepository.findAccountByCardNumber(form.getCardNumber())
				.orElseThrow(() -> new BusinessException(NUM_CARTAO_INVALIDO));

		if (Boolean.FALSE.equals(account.getIsActive()) || Boolean.FALSE.equals(account.getCard().getIsActive())) {
			throw new BusinessException(CONTA_INATIVA);
		}
		BigDecimal limiteCreditoUtilizado = transactionRepository.calculateUsedCreditLimit(account.getId());
		if (account.getCreditLimit().subtract(limiteCreditoUtilizado).compareTo(form.getAmount()) < 0) {
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

	private void processWithdrawal(TransactionFormVO form) {
		Account account = accountRepository.findAccountByCardNumber(form.getCardNumber())
				.orElseThrow(() -> new BusinessException(NUM_CARTAO_INVALIDO));

		if (Boolean.FALSE.equals(account.getIsActive()) || Boolean.FALSE.equals(account.getCard().getIsActive())) {
			throw new BusinessException(CONTA_INATIVA);
		}
		BigDecimal limiteCreditoUtilizado = transactionRepository.calculateUsedCreditLimit(account.getId());
		BigDecimal limiteSaqueUtilizado = transactionRepository.calculateUsedWithdrawalLimit(account.getId());
		if (account.getCreditLimit().subtract(limiteCreditoUtilizado).compareTo(form.getAmount()) < 0) {
			throw new BusinessException(LIMITE_CREDITO_EXCEDIDO);
		} else if (account.getWithdrawalLimit().subtract(limiteSaqueUtilizado).compareTo(form.getAmount()) < 0) {
			throw new BusinessException(LIMITE_SAQUE_EXCEDIDO);
		}
		Transaction transaction = Transaction.builder()
				.account(account)
				.type(form.getType())
				.description(form.getDescription())
				.amount(form.getAmount().negate())
				.eventDate(LocalDateTime.now())
				.build();

		transactionRepository.save(transaction);
	}

	private void processPayment(TransactionFormVO form) {
		Invoice invoice = invoiceRepository.findByInvoiceNumber(form.getInvoiceNumber())
				.orElseThrow(() -> new BusinessException(NUM_FATURA_INVALIDO));

		if (!InvoiceStatus.AGUARDANDO_PAGAMENTO.equals(invoice.getStatus())) {
			throw new BusinessException(FATURA_INDISP_PAGAMENTO);
		}

		Account account = invoice.getAccount();
		if (Boolean.FALSE.equals(account.getIsActive()) || Boolean.FALSE.equals(account.getCard().getIsActive())) {
			throw new BusinessException(CONTA_INATIVA);
		}

		if (form.getAmount().compareTo(invoice.getPaymentDue()) == 0) {
			Transaction transaction = Transaction.builder()
					.account(account)
					.type(form.getType())
					.description(concat("|", PAGAMENTO_TOTAL_FATURA, form.getDescription()))
					.amount(form.getAmount())
					.eventDate(LocalDateTime.now())
					.build();

			transactionRepository.save(transaction);
			invoice.setStatus(InvoiceStatus.PAGO_TOTALMENTE);
			invoice.getTransactions().add(transaction);
			invoiceRepository.save(invoice);

		} else {

			BigDecimal valorPago;
			InvoiceStatus statusFatura;
			String descricao;
			if (form.getAmount().compareTo(invoice.getPaymentDue()) > 0) {
				valorPago = invoice.getPaymentDue();
				statusFatura = InvoiceStatus.PAGO_TOTALMENTE;
				descricao = PAGAMENTO_TOTAL_FATURA;
			} else {
				valorPago = form.getAmount();
				statusFatura = InvoiceStatus.PAGO_PARCIALMENTE;
				descricao = PAGAMENTO_PARCIAL_FATURA;
			}

			Transaction transactionPagamento = Transaction.builder()
					.account(account)
					.type(form.getType())
					.description(concat("|", descricao, form.getDescription()))
					.amount(valorPago)
					.eventDate(LocalDateTime.now())
					.build();

			transactionRepository.save(transactionPagamento);
			invoice.setStatus(statusFatura);
			invoice.getTransactions().add(transactionPagamento);
			invoiceRepository.save(invoice);

			BigDecimal valorDiferenca = form.getAmount().subtract(invoice.getPaymentDue());
			String descDiferenca = (valorDiferenca.compareTo(BigDecimal.ZERO) > 0) ? CREDITO_FATURA_ANTERIOR : SALDO_DEVEDOR_FATURA_ANTERIOR;

			Transaction transactionDiferenca = Transaction.builder()
					.account(account)
					.type(form.getType())
					.description(descDiferenca)
					.amount(valorDiferenca)
					.eventDate(LocalDateTime.now())
					.build();

			transactionRepository.save(transactionDiferenca);
		}
	}
}
