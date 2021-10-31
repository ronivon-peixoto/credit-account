package io.pismo.creditaccount.model.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.pismo.creditaccount.model.converter.BooleanToStringConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "card_number", nullable = false, length = 16)
	private String cardNumber;

	@Convert(converter = BooleanToStringConverter.class)
	@Column(name = "is_active", nullable = false, length = 1)
	private Boolean isActive;

}
