package com.wynd.vop.framework.test.exception;

/**
 * The Class BipTestLibException.
 */
public class BipTestLibException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new vop test lib exception.
	 *
	 * @param message the message
	 * @param t the t
	 */
	public BipTestLibException(final String message, final Throwable t) {
		super(message, t);
	}

	/**
	 * Instantiates a new vop test lib exception.
	 *
	 * @param message the message
	 */
	public BipTestLibException(final String message) {
		super(message);
	}

}
