package io.pismo.creditaccount.service.impl.strategy;

import static io.pismo.creditaccount.utils.StringUtil.concat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.pismo.creditaccount.data.vo.form.TransactionFormVO;
import io.pismo.creditaccount.model.entity.Account;
import io.pismo.creditaccount.model.entity.Invoice;
import io.pismo.creditaccount.model.entity.Transaction;
import io.pismo.creditaccount.model.enums.InvoiceStatus;
import io.pismo.creditaccount.model.enums.TransactionType;
import io.pismo.creditaccount.model.repository.InvoiceRepository;
import io.pismo.creditaccount.model.repository.TransactionRepository;
import io.pismo.creditaccount.rest.exception.BusinessException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentTransaction implements TransactionProcessor {

	private TransactionRepository transactionRepository;
	private InvoiceRepository invoiceRepository;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void process(TransactionFormVO form) {
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

			BigDecimal amountPaid;
			InvoiceStatus invoiceStatus;
			String description;

			if (form.getAmount().compareTo(invoice.getPaymentDue()) > 0) {
				amountPaid = invoice.getPaymentDue();
				invoiceStatus = InvoiceStatus.PAGO_TOTALMENTE;
				description = PAGAMENTO_TOTAL_FATURA;
			} else {
				amountPaid = form.getAmount();
				invoiceStatus = InvoiceStatus.PAGO_PARCIALMENTE;
				description = PAGAMENTO_PARCIAL_FATURA;
			}

			Transaction paymentTransaction = Transaction.builder()
					.account(account)
					.type(form.getType())
					.description(concat("|", description, form.getDescription()))
					.amount(amountPaid)
					.eventDate(LocalDateTime.now())
					.build();

			transactionRepository.save(paymentTransaction);
			invoice.setStatus(invoiceStatus);
			invoice.getTransactions().add(paymentTransaction);
			invoiceRepository.save(invoice);

			BigDecimal amountPaidDifference = form.getAmount().subtract(invoice.getPaymentDue());
			String descriptionDifference = (amountPaidDifference.compareTo(BigDecimal.ZERO) > 0) ? CREDITO_FATURA_ANTERIOR : SALDO_DEVEDOR_FATURA_ANTERIOR;

			Transaction differenceTransaction = Transaction.builder()
					.account(account)
					.type(form.getType())
					.description(concat("", descriptionDifference))
					.amount(amountPaidDifference)
					.eventDate(LocalDateTime.now())
					.build();

			transactionRepository.save(differenceTransaction);
		}
	}

	@Override
	public TransactionType getTransactionType() {
		return TransactionType.PAGAMENTO;
	}

}
