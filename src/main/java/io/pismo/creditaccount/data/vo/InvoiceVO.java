package io.pismo.creditaccount.data.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.pismo.creditaccount.model.entity.Invoice;
import io.pismo.creditaccount.model.enums.InvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "id", "account", "status", "invoiceNumber", "paymentDueDate", "paymentDue", "transactions" })
public class InvoiceVO implements Serializable {

	private static final long serialVersionUID = -7517105742288514369L;

	private Long id;

	@JsonBackReference
	private AccountVO account;

	private InvoiceStatus status;

	private String invoiceNumber;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private LocalDate paymentDueDate;

	private BigDecimal paymentDue;

	@JsonManagedReference
	private List<TransactionVO> transactions;

	public static InvoiceVO create(Invoice invoice) {
		return new ModelMapper().map(invoice, InvoiceVO.class);
	}
}
