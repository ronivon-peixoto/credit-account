package io.pismo.creditaccount.model.enums;

public enum TransactionType {

	COMPRA_A_VISTA("Compra à vista", true), 
	COMPRA_PARCELADA("Compra parcelada", true), 
	SAQUE("Saque", true),
	PAGAMENTO("Pagamento", true), 
	CREDITO_PAGAMENTO_EXCEDENTE("Crédito de pagamento excedente", false),
	PENDENCIA_FATURA_ANTERIOR("Pendência da fatura anterior", false);

	private String description;
	private boolean visible;

	private TransactionType(String description, boolean visible) {
		this.description = description;
		this.visible = visible;
	}

	public String getDescription() {
		return description;
	}

	public boolean isVisible() {
		return visible;
	}

}
