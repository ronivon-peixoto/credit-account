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
import javax.persistence.Table;

import org.modelmapper.ModelMapper;

import io.pismo.creditaccount.data.vo.TransactionVO;
import io.pismo.creditaccount.model.enums.TransactionType;
import lombok.Data;

@Data
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

	@Column(name = "description", nullable = false, length = 45)
	private String description;

	@Column(name = "amount", nullable = false, precision = 15, scale = 2)
	private BigDecimal amount;

	@Column(name = "event_date", columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
	private LocalDateTime eventDate;

	public static Transaction create(TransactionVO transactionVO) {
		return new ModelMapper().map(transactionVO, Transaction.class);
	}
}
