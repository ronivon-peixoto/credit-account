package io.pismo.creditaccount.data.vo.form;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "docNumber", "creditLimit", "withdrawalLimit", "invoiceClosingDay" })
public class AccountFormVO implements Serializable {

	private static final long serialVersionUID = -2277146150282225496L;

	@NotNull(message = "{field.docNumber.required}")
	@CPF(message = "{field.docNumber.invalid}")
	private String docNumber;

	@NotNull(message = "{field.creditLimit.required}")
	private BigDecimal creditLimit;

	@NotNull(message = "{field.withdrawalLimit.required}")
	private BigDecimal withdrawalLimit;

	@NotNull(message = "{field.invoiceClosingDay.required}")
	private Integer invoiceClosingDay;

}
