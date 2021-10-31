package io.pismo.creditaccount.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BooleanToStringConverter implements AttributeConverter<Boolean, String> {

	private static final String YES = "S";
	private static final String NO = "N";

	@Override
	public String convertToDatabaseColumn(Boolean value) {
		if (value != null && value.booleanValue()) {
			return YES;
		}
		return NO;
	}

	@Override
	public Boolean convertToEntityAttribute(String value) {
		if (value != null && value.equalsIgnoreCase(YES)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}
