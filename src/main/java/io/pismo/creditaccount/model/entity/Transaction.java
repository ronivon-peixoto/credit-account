package io.pismo.creditaccount.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

import org.modelmapper.ModelMapper;

import io.pismo.creditaccount.data.vo.TransactionVO;
import io.pismo.creditaccount.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@NamedNativeQueries(value = {
	@NamedNativeQuery(name = "Transaction.calculateUsedCreditLimit", query = "SELECT ABS(COALESCE(SUM(t.amount),0)) FROM transaction t LEFT JOIN invoice_has_transactions iht ON iht.transaction_id = t.id LEFT JOIN invoice i ON i.id = iht.invoice_id WHERE (iht.invoice_id IS NULL OR i.invoice_status = 'AGUARDANDO_PAGAMENTO') AND t.account_id = ?1"),
	@NamedNativeQuery(name = "Transaction.calculateUsedWithdrawalLimit", query = "SELECT ABS(COALESCE(SUM(t.amount),0)) FROM transaction t LEFT JOIN invoice_has_transactions iht ON iht.transaction_id = t.id LEFT JOIN invoice i ON i.id = iht.invoice_id WHERE (iht.invoice_id IS NULL OR i.invoice_status = 'AGUARDANDO_PAGAMENTO') AND t.operation_type = 'SAQUE' AND t.account_id = ?1"),
	@NamedNativeQuery(name = "Transaction.listPendingTransactionsByAccountID", query = "SELECT t.id, t.account_id, t.operation_type, t.description, t.amount, t.event_date FROM transaction t LEFT JOIN invoice_has_transactions iht ON iht.transaction_id = t.id WHERE iht.invoice_id IS NULL AND t.event_date <= CURRENT_TIMESTAMP() AND t.account_id = ?1", resultClass = Transaction.class),
})
@Entity
@Table(name = "transaction")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false)
	private Account account;

	@Enumerated(EnumType.STRING)
	@Column(name = "operation_type", nullable = false, length = 20)
	private TransactionType type;

	@Column(name = "description", nullable = false, length = 100)
	private String description;

	@Column(name = "amount", nullable = false, precision = 15, scale = 2)
	private BigDecimal amount;

	@Column(name = "event_date", columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
	private LocalDateTime eventDate;

	public static Transaction create(TransactionVO transactionVO) {
		return new ModelMapper().map(transactionVO, Transaction.class);
	}

}
