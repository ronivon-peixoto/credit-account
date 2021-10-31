package io.pismo.creditaccount.model.enums;

public enum InvoiceStatus {

	AGUARDANDO_PAGAMENTO("Aguardando pagamento"), 
	PAGO_TOTALMENTE("Pago totalmente"),
	PAGO_PARCIALMENTE("Pago parcialmente"), 
	NAO_PAGO("Pagamento não realizado");

	private String description;

	private InvoiceStatus(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
