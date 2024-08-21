package com.wynd.vop.framework.security.jwt.correlation;

import com.wynd.vop.framework.exception.BipRuntimeException;
import com.wynd.vop.framework.log.BipLogger;
import com.wynd.vop.framework.log.BipLoggerFactory;
import com.wynd.vop.framework.messages.MessageKeys;
import com.wynd.vop.framework.messages.MessageSeverity;
import org.springframework.http.HttpStatus;

/**
 * Vaules for user status codes.
 */
public enum UserStatus {

	/** Status of permanent */
	PERMANENT("P"),
	/** Status of active */
	ACTIVE("A"),
	/** Status of temporary */
	TEMPORARY("T");

	private static final BipLogger LOGGER = BipLoggerFactory.getLogger(UserStatus.class);

	/** The arbitrary string value of the enumeration */
	private String status;

	/**
	 * Private constructor for enum initialization
	 *
	 * @param status String
	 */
	private UserStatus(final String status) {
		this.status = status;
	}

	/**
	 * The arbitrary String value assigned to the enumeration.
	 *
	 * @return String
	 */
	public String value() {
		return status;
	}

	/**
	 * Get the enumeration for the associated arbitrary String value.
	 * Throws a runtime exception if the string value does not match one of the enumeration values.
	 *
	 * @param stringValue the string value
	 * @return UserStatus - the enumeration
	 * @throws BipRuntimeException if no match of enumeration values
	 */
	public static UserStatus fromValue(final String stringValue) {
		for (UserStatus s : UserStatus.values()) {
			if (s.value().equals(stringValue)) {
				return s;
			}
		}
		MessageKeys key = MessageKeys.VOP_SECURITY_TRAITS_USERSTATUS_INVALID;
		String[] params = new String[] { stringValue };
		LOGGER.error(key.getMessage(params));
		throw new BipRuntimeException(key, MessageSeverity.ERROR, HttpStatus.BAD_REQUEST, params);
	}

}
