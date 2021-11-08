package io.pismo.creditaccount.service.impl.strategy;

import io.pismo.creditaccount.data.vo.form.TransactionFormVO;
import io.pismo.creditaccount.model.enums.TransactionType;

public interface TransactionProcessor {

	static final String PAGAMENTO_TOTAL_FATURA = "PAGAMENTO TOTAL DA FATURA";
	static final String PAGAMENTO_PARCIAL_FATURA = "PAGAMENTO PARCIAL DA FATURA";
	static final String CREDITO_FATURA_ANTERIOR = "CRÉDITO FATURA ANTERIOR";
	static final String SALDO_DEVEDOR_FATURA_ANTERIOR = "SALDO DEVEDOR FATURA ANTERIOR";

	static final String NUM_CARTAO_INVALIDO = "Card number is invalid.";
	static final String NUM_FATURA_INVALIDO = "Invoice number is invalid.";
	static final String CONTA_INATIVA = "User account is inactive.";
	static final String FATURA_INDISP_PAGAMENTO = "Invoice unavailable for payment.";
	static final String LIMITE_CREDITO_EXCEDIDO = "Credit limit exceeded.";
	static final String LIMITE_SAQUE_EXCEDIDO = "Exceeded cash withdrawal limit.";

	void process(TransactionFormVO form);

	TransactionType getTransactionType();

}
