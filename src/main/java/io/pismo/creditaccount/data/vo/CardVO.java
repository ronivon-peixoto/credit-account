package io.pismo.creditaccount.data.vo;

import java.io.Serializable;

import org.modelmapper.ModelMapper;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.pismo.creditaccount.model.entity.Card;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "id", "cardNumber", "isActive" })
public class CardVO implements Serializable {

	private static final long serialVersionUID = 7299013908314828977L;

	private Long id;

	private String cardNumber;

	private Boolean isActive;

	public static CardVO create(Card card) {
		return new ModelMapper().map(card, CardVO.class);
	}

}
