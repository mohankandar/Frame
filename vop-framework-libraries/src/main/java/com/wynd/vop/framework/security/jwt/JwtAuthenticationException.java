package com.wynd.vop.framework.security.jwt;

import com.wynd.vop.framework.exception.BipExceptionData;
import com.wynd.vop.framework.exception.BipExceptionExtender;
import com.wynd.vop.framework.messages.MessageKey;
import com.wynd.vop.framework.messages.MessageKeys;
import com.wynd.vop.framework.messages.MessageSeverity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

/**
 * Created by vgadda on 5/4/17.
 */
public class JwtAuthenticationException extends AuthenticationException implements
    BipExceptionExtender {
	private static final long serialVersionUID = 7005295148966720986L;

	/** The {@link BipExceptionData} object*/
	private final BipExceptionData exceptionData;

	/**
	 * Constructs a new RuntimeException with the specified detail key, message, severity, and status.
	 * The cause is not initialized, and may subsequently be initialized by
	 * a call to {@link #initCause}.
	 *
	 * @see RuntimeException#RuntimeException(String)
	 *
	 * @param key - the consumer-facing key that can uniquely identify the nature of the exception
	 * @param severity - the severity of the event: FATAL (500 series), ERROR (400 series), WARN (200 series), or INFO/DEBUG/TRACE
	 * @param status - the HTTP Status code that applies best to the encountered problem, see
	 *            <a href="https://tools.ietf.org/html/rfc7231">https://tools.ietf.org/html/rfc7231</a>
	 * @param params - arguments to fill in any params in the MessageKey message (e.g. value for {0})
	 */
	public JwtAuthenticationException(final MessageKey key, final MessageSeverity severity, final HttpStatus status,
			final String... params) {
		this(key, severity, status, null, params);
	}

	/**
	 * Constructs a new RuntimeException with the specified detail key, message, severity, status, and cause.
	 *
	 * @see RuntimeException#RuntimeException(String, Throwable)
	 *
	 * @param key - the consumer-facing key that can uniquely identify the nature of the exception
	 * @param severity - the severity of the event: FATAL (500 series), ERROR (400 series), WARN (200 series), or INFO/DEBUG/TRACE
	 * @param status - the HTTP Status code that applies best to the encountered problem, see
	 *            <a href="https://tools.ietf.org/html/rfc7231">https://tools.ietf.org/html/rfc7231</a>
	 * @param cause - the throwable that caused this throwable
	 * @param params - arguments to fill in any params in the MessageKey message (e.g. value for {0})
	 */
	public JwtAuthenticationException(final MessageKey key, final MessageSeverity severity, final HttpStatus status,
			final Throwable cause, final String... params) {
		super((key == null ? MessageKeys.NO_KEY.toString() : key.getMessage(params)), cause);
		exceptionData = new BipExceptionData(key, severity, status, params);
	}

	/**
	 * Returns the VOP Exception Data.
	 *
	 * @return the exception data
	 * @see BipExceptionData
	 */
	@Override
	public BipExceptionData getExceptionData() {
		return exceptionData;
	}

}
