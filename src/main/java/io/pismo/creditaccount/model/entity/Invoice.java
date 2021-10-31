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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.pismo.creditaccount.model.enums.InvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonBackReference
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

	@JsonManagedReference
	@OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Transaction> transactions;

}