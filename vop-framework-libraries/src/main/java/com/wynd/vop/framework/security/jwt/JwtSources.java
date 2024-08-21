package com.wynd.vop.framework.security.jwt;

import com.wynd.vop.framework.exception.BipRuntimeException;
import com.wynd.vop.framework.log.BipLogger;
import com.wynd.vop.framework.log.BipLoggerFactory;
import com.wynd.vop.framework.messages.MessageKeys;
import com.wynd.vop.framework.messages.MessageSeverity;
import com.wynd.vop.framework.security.jwt.correlation.UserStatus;
import org.springframework.http.HttpStatus;

public enum JwtSources {


    VOP("VOP"),
    KONG("KONG");

    private static final BipLogger LOGGER = BipLoggerFactory.getLogger(UserStatus.class);

    /** The arbitrary string value of the enumeration */
    private String jwtSource;

    /**
     * Private constructor for enum initialization
     *
     * @param jwtSource String
     */
    private JwtSources(final String jwtSource) {
        this.jwtSource = jwtSource;
    }

    /**
     * The arbitrary String value assigned to the enumeration.
     *
     * @return String
     */
    public String value() {
        return jwtSource;
    }

    /**
     * Get the enumeration for the associated arbitrary String value.
     * Throws a runtime exception if the string value does not match one of the enumeration values.
     *
     * @param stringValue the string value
     * @return JwtSources - the enumeration
     * @throws BipRuntimeException if no match of enumeration values
     */
    public static JwtSources fromValue(final String stringValue) {
        for (JwtSources s : JwtSources.values()) {
            if (s.value().equals(stringValue)) {
                return s;
            }
        }
        MessageKeys key = MessageKeys.VOP_SECURITY_TRAITS_JWTSOURCE_INVALID;
        String[] params = new String[] { stringValue };
        LOGGER.error(key.getMessage(params));
        throw new BipRuntimeException(key, MessageSeverity.ERROR, HttpStatus.BAD_REQUEST, params);
    }


}
