package io.pismo.creditaccount.data.vo.form;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.pismo.creditaccount.data.validation.TransactionValidate;
import io.pismo.creditaccount.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TransactionValidate
@JsonPropertyOrder({ "cardNumber", "invoiceNumber", "description", "type", "amount", "installments" })
public class TransactionFormVO implements Serializable {

	private static final long serialVersionUID = 514644493295413824L;

	private String cardNumber;

	private String invoiceNumber;

	private String description;

	private TransactionType type;

	private BigDecimal amount;

	private Integer installments;

}
