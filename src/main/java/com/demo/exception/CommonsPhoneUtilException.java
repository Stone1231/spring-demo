package com.demo.exception;

public class CommonsPhoneUtilException extends CommonsRuntimeException {

	private static final long serialVersionUID = -7533654492038599563L;

	public CommonsPhoneUtilException(Throwable cause) {
		super(cause);
	}

	public CommonsPhoneUtilException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommonsPhoneUtilException(String message) {
		super(message);
	}

}
