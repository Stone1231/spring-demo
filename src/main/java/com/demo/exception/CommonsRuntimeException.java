package com.demo.exception;

public class CommonsRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -7443771526198281248L;

	public CommonsRuntimeException() {
		super();
	}

	public CommonsRuntimeException(String message) {
		super(message);
	}

	public CommonsRuntimeException(Throwable cause) {
		super(cause);
	}

	public CommonsRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

}
