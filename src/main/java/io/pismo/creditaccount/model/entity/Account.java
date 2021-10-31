package io.pismo.creditaccount.model.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.modelmapper.ModelMapper;

import io.pismo.creditaccount.data.vo.AccountVO;
import io.pismo.creditaccount.model.converter.BooleanToStringConverter;
import lombok.Data;

@Data
@Entity
@Table(name = "account")
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "doc_number", nullable = false, length = 11)
	private String docNumber;

	@Column(name = "credit_limit", nullable = false, precision = 15, scale = 2)
	private BigDecimal creditLimit;

	@Column(name = "withdrawal_limit", nullable = false, precision = 15, scale = 2)
	private BigDecimal withdrawalLimit;

	@OneToOne
	@JoinColumn(name = "card_id", referencedColumnName = "id", nullable = false)
	private Card card;

	@Convert(converter = BooleanToStringConverter.class)
	@Column(name = "is_active", nullable = false, length = 1)
	private Boolean isActive;

	public static Account create(AccountVO accountVO) {
		return new ModelMapper().map(accountVO, Account.class);
	}
}
