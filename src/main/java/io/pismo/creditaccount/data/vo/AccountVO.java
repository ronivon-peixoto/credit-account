package io.pismo.creditaccount.data.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import org.modelmapper.ModelMapper;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.pismo.creditaccount.model.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "id", "docNumber", "creditLimit", "withdrawalLimit", "card", "isActive" })
public class AccountVO implements Serializable {

	private static final long serialVersionUID = 5570856633046067283L;

	private Long id;

	private String docNumber;

	private BigDecimal creditLimit;

	private BigDecimal withdrawalLimit;

	private CardVO card;

	private Boolean isActive;

	public static AccountVO create(Account account) {
		return new ModelMapper().map(account, AccountVO.class);
	}

}
