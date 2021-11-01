package io.pismo.creditaccount.data.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.pismo.creditaccount.model.entity.Transaction;
import io.pismo.creditaccount.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "id", "account", "type", "description", "amount", "eventDate" })
public class TransactionVO implements Serializable {

	private static final long serialVersionUID = 8105622604027745349L;

	private Long id;

	@JsonBackReference
	private AccountVO account;

	private TransactionType type;

	private String description;

	private BigDecimal amount;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime eventDate;

	public static TransactionVO create(Transaction transaction) {
		return new ModelMapper().map(transaction, TransactionVO.class);
	}
}
