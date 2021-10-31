package io.pismo.creditaccount.data.vo.form;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.pismo.creditaccount.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "cardNumber", "invoiceNumber", "description", "type", "amount", "installments" })
public class TransactionFormVO implements Serializable {

	private static final long serialVersionUID = 514644493295413824L;

	@NotEmpty(message = "{field.cardNumber.required}")
	private String cardNumber;

	@NotEmpty(message = "{field.invoiceNumber.required}")
	private String invoiceNumber;

	@NotEmpty(message = "{field.description.required}")
	private String description;

	@NotNull(message = "{field.type.required}")
	private TransactionType type;

	@NotNull(message = "{field.amount.required}")
	private BigDecimal amount;

	@NotNull(message = "{field.installments.required}")
	private Integer installments;

}
