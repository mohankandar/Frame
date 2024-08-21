package com.wynd.vop.framework.security.jwt.correlation;

import com.wynd.vop.framework.exception.BipRuntimeException;
import com.wynd.vop.framework.log.BipLogger;
import com.wynd.vop.framework.log.BipLoggerFactory;
import com.wynd.vop.framework.messages.MessageKeys;
import com.wynd.vop.framework.messages.MessageSeverity;
import org.springframework.http.HttpStatus;

public enum IdTypes {
	/** The type of correlation id for national authorities */
	NATIONAL("NI"),
	/** The type of correlation id for patient authorities */
	PATIENT("PI"),
	/** The pnidType (currently the only one) associated with the PNID field value */
	SOCIAL("SS");

	private static final BipLogger LOGGER = BipLoggerFactory.getLogger(IdTypes.class);

	/** The arbitrary string value of the enumeration */
	private String type;

	/**
	 * Private constructor for enum initialization
	 *
	 * @param type String
	 */
	private IdTypes(String type) {
		this.type = type;
	}

	/**
	 * The arbitrary String value assigned to the enumeration.
	 *
	 * @return String
	 */
	public String value() {
		return this.type;
	}

	/**
	 * Get the enumeration for the associated arbitrary String value.
	 * Throws a runtime exception if the string value does not match one of the enumeration values.
	 *
	 * @param stringValue the string value
	 * @return IdTypes - the enumeration
	 * @throws BipRuntimeException if no match of enumeration values
	 */
	public static IdTypes fromValue(final String stringValue) {
		for (IdTypes s : IdTypes.values()) {
			if (s.value().equals(stringValue)) {
				return s;
			}
		}
		MessageKeys key = MessageKeys.VOP_SECURITY_TRAITS_IDTYPE_INVALID;
		String[] params = new String[] { stringValue };
		LOGGER.error(key.getMessage(params));
		throw new BipRuntimeException(key, MessageSeverity.ERROR, HttpStatus.BAD_REQUEST, params);
	}
}
