package com.wynd.vop.framework.exception;

/**
 * The root VOP interface for managing throwables, runtime exceptions, and checked exceptions.
 * <p>
 * To support the requirements of consumer responses, all VOP Exception classes should
 * implement this interface.
 * <p>
 * 
 * Implementers of this interface should provide two constructors in the form of:
 * <table style="border-collapse:collapse;">
 * <tr>
 * <td></td>
 * <td>{@code public BipYourException(String key, String message, MessageSeverity severity, HttpStatus status)}</td>
 * </tr>
 * <tr>
 * <td>and</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>{@code public BipYourException(String key, String message, MessageSeverity severity, HttpStatus status, Throwable cause)}</td>
 * </tr>
 * </table>
 *
 * @author aburkholder
 */
public interface BipExceptionExtender {

	/**
	 * Getter for {@link BipExceptionData} object
	 * 
	 * @return the {@link BipExceptionData} object
	 */
	public BipExceptionData getExceptionData();

}
