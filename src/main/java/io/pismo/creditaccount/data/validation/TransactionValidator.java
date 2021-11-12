package io.pismo.creditaccount.data.validation;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import io.pismo.creditaccount.data.vo.form.TransactionFormVO;
import io.pismo.creditaccount.model.enums.TransactionType;

public class TransactionValidator implements ConstraintValidator<TransactionValidate, TransactionFormVO> {

	@Override
	public boolean isValid(TransactionFormVO form, ConstraintValidatorContext context) {
		if (form == null) {
			return Boolean.FALSE;
		} else if (form.getType() == null) { // TYPE
			addConstraintViolation("The type is required.", context);
			return Boolean.FALSE;
		}

		boolean retorno = Boolean.TRUE;

		// CARD_NUMBER
		if (!TransactionType.PAGAMENTO.equals(form.getType()) && isEmpty(form.getCardNumber())) {
			addConstraintViolation("The card number is required.", context);
			retorno = Boolean.FALSE;
		}

		// INVOICE_NUMBER
		if (TransactionType.PAGAMENTO.equals(form.getType())) {
			if (isEmpty(form.getInvoiceNumber())) {
				addConstraintViolation("The invoice number is required.", context);
				retorno = Boolean.FALSE;
			}
		} else if (!isEmpty(form.getInvoiceNumber())) {
			addConstraintViolation("The invoice number must not be informed.", context);
			retorno = Boolean.FALSE;
		}

		// DESCRIPTION
		if (isEmpty(form.getDescription()) || form.getDescription().length() > 50) {
			addConstraintViolation("Describe the transaction in up to 50 characters.", context);
			retorno = Boolean.FALSE;
		}

		// INSTALLMENTS
		if (TransactionType.COMPRA_PARCELADA.equals(form.getType())) {
			if(form.getInstallments() == null || form.getInstallments() <= 1) {
				addConstraintViolation("The installments must be informed and must be greater than 1.", context);
				retorno = Boolean.FALSE;
			}
		} else if (form.getInstallments() != null) {
			addConstraintViolation("The installments payment must not be informed.", context);
			retorno = Boolean.FALSE;
		}

		// AMOUNT
		if (form.getAmount() == null || form.getAmount().equals(BigDecimal.ZERO)) {
			addConstraintViolation("The amount is required.", context);
			retorno = Boolean.FALSE;
		}

		return retorno;
	}

	private void addConstraintViolation(String message, ConstraintValidatorContext ctx) {
		ctx.disableDefaultConstraintViolation();
		ctx.buildConstraintViolationWithTemplate(message).addConstraintViolation();
	}

}