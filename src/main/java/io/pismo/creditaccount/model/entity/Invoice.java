package io.pismo.creditaccount.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.modelmapper.ModelMapper;

import io.pismo.creditaccount.data.vo.InvoiceVO;
import io.pismo.creditaccount.model.enums.InvoiceStatus;
import lombok.Data;

@Data
@Entity
@Table(name = "invoice")
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false)
	private Account account;

	@Enumerated(EnumType.STRING)
	@Column(name = "invoice_status", nullable = false, length = 20)
	private InvoiceStatus status;

	@Column(name = "invoice_number", nullable = false, length = 48)
	private String invoiceNumber;

	@Column(name = "payment_due_date", nullable = false, columnDefinition = "DATE")
	private LocalDate paymentDueDate;

	@Column(name = "payment_due", nullable = false, precision = 15, scale = 2)
	private BigDecimal paymentDue;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(name = "invoice_has_transactions", 
		joinColumns = { @JoinColumn(name = "invoice_id") }, 
		inverseJoinColumns = { @JoinColumn(name = "transaction_id") })
	private List<Transaction> transactions;

	public static Invoice create(InvoiceVO invoiceVO) {
		return new ModelMapper().map(invoiceVO, Invoice.class);
	}
}
