package io.pismo.creditaccount.model.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.modelmapper.ModelMapper;

import io.pismo.creditaccount.data.vo.CardVO;
import io.pismo.creditaccount.model.converter.BooleanToStringConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "card")
public class Card {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "card_number", unique = true, nullable = false, length = 16)
	private String cardNumber;

	@Convert(converter = BooleanToStringConverter.class)
	@Column(name = "is_active", nullable = false, length = 1)
	private Boolean isActive;

	public static Card create(CardVO cardVO) {
		return new ModelMapper().map(cardVO, Card.class);
	}

}
