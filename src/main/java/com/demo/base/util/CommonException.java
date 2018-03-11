package com.demo.base.util;

/**
 * The Class BaseException.
 */
public class CommonException extends Exception {

	/** The Constant serialVersionUID. */

	private static final long serialVersionUID = 621609046335254828L;

	/**
	 * Instantiates a new base exception.
	 */
	public CommonException() {
		super();
	}

	/**
	 * Instantiates a new base exception.
	 *
	 * @param message
	 *            the message
	 */
	public CommonException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new base exception.
	 *
	 * @param cause
	 *            the cause
	 */
	public CommonException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new base exception.
	 *
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public CommonException(String message, Throwable cause) {
		super(message, cause);
	}

}
