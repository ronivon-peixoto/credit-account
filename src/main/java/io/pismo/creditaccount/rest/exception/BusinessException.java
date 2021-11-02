package io.pismo.creditaccount.rest.exception;

public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = -2379443631526239966L;

	public BusinessException() {
		super();
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

}
