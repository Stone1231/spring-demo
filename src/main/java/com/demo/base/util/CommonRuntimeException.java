package com.demo.base.util;

/**
 * The Class BaseRuntimeException.
 */
public class CommonRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -6239966567541107912L;

	/** The Constant serialVersionUID. */

	/**
	 * Instantiates a new base runtime exception.
	 */
	public CommonRuntimeException() {
		super();
	}

	/**
	 * Instantiates a new base runtime exception.
	 *
	 * @param message
	 *            the message
	 */
	public CommonRuntimeException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new base runtime exception.
	 *
	 * @param cause
	 *            the cause
	 */
	public CommonRuntimeException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new base runtime exception.
	 *
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public CommonRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

}
