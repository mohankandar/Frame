package com.wynd.vop.framework.test.exception;

/**
 * Custom runtime exception for VOP application that extends RuntimeException.
 * Application use this test library will throw this exception for any runtime exception.
 * 
 */
public class BipTestLibRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new vop test lib runtime exception.
	 *
	 * @param message the message
	 * @param t the throwable
	 */
	public BipTestLibRuntimeException(final String message, final Throwable t) {
		super(message, t);
	}

	/**
	 * Instantiates a new vop test lib runtime exception.
	 *
	 * @param message the message
	 */
	public BipTestLibRuntimeException(final String message) {
		super(message);
	}

}
