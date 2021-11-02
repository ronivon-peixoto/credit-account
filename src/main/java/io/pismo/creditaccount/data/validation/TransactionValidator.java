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
			addConstraintViolation("O campo type é obrigatório.", context);
			return Boolean.FALSE;
		}

		boolean retorno = Boolean.TRUE;

		// CARD_NUMBER
		if (!TransactionType.PAGAMENTO.equals(form.getType()) && isEmpty(form.getCardNumber())) {
			addConstraintViolation("O campo cardNumber é obrigatório.", context);
			retorno = Boolean.FALSE;
		}

		// INVOICE_NUMBER
		if (TransactionType.PAGAMENTO.equals(form.getType())) {
			if (isEmpty(form.getInvoiceNumber())) {
				addConstraintViolation("O campo invoiceNumber é obrigatório.", context);
				retorno = Boolean.FALSE;
			}
		} else if (!isEmpty(form.getInvoiceNumber())) {
			addConstraintViolation("O campo invoiceNumber não deve ser informado.", context);
			retorno = Boolean.FALSE;
		}

		// DESCRIPTION
		if (form.getDescription() != null && form.getDescription().length() > 50) {
			addConstraintViolation("O campo description pode conter até 50 caracteres.", context);
			retorno = Boolean.FALSE;
		}

		// INSTALLMENTS
		if (TransactionType.COMPRA_PARCELADA.equals(form.getType())) {
			if(form.getInstallments() == null || form.getInstallments() <= 1) {
				addConstraintViolation("O campo installments é obrigatório e deve ter valor maior que 1.", context);
				retorno = Boolean.FALSE;
			}
		} else if (form.getInstallments() != null) {
			addConstraintViolation("O campo installments não deve ser informado.", context);
			retorno = Boolean.FALSE;
		}

		// AMOUNT
		if (form.getAmount() == null || form.getAmount().equals(BigDecimal.ZERO)) {
			addConstraintViolation("O campo amount é obrigatório.", context);
			retorno = Boolean.FALSE;
		}

		return retorno;
	}

	private void addConstraintViolation(String message, ConstraintValidatorContext ctx) {
		ctx.disableDefaultConstraintViolation();
		ctx.buildConstraintViolationWithTemplate(message).addConstraintViolation();
	}

}