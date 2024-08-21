package com.wynd.vop.framework.security.jwt;

import com.wynd.vop.framework.log.BipLogger;
import com.wynd.vop.framework.log.BipLoggerFactory;
import com.wynd.vop.framework.messages.MessageKeys;
import com.wynd.vop.framework.messages.MessageSeverity;
import com.wynd.vop.framework.security.PersonTraits;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Provider for decrypting and parsing the JWT.
 */
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	/** Constant for the logger for this class */
	private static final BipLogger vopLogger = BipLoggerFactory.getLogger(JwtAuthenticationProvider.class);

	/** parses the token into a set of security "claims" contained in the token */
	JwtParser parser;

	@Value("${vop.framework.security.jwt.validation.required-parameters:}")
	private String[] jwtTokenRequiredParameterList;

	/**
	 * Create the provider.
	 *
	 * @param parser - the JWT parser
	 */
	public JwtAuthenticationProvider(final JwtParser parser) {
		this.parser = parser;
	}

	@Override
	protected void additionalAuthenticationChecks(final UserDetails userDetails,
			final UsernamePasswordAuthenticationToken authentication) {
		// no additional checks for authentication
	}

	@Override
	protected UserDetails retrieveUser(final String username, final UsernamePasswordAuthenticationToken authentication) {
		final JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
		final String token = authenticationToken.getToken();// pass this for verification

		final PersonTraits person = parser.parseJwt(token);

		if (person == null) {
			throw new JwtAuthenticationException(MessageKeys.VOP_SECURITY_TOKEN_INVALID,
					MessageSeverity.ERROR, HttpStatus.BAD_REQUEST);
		}

		if (!isPersonTraitsValid(person, jwtTokenRequiredParameterList)) {
			throw new JwtAuthenticationException(MessageKeys.VOP_SECURITY_TOKEN_INVALID_REQ_PARAM_MISSING,
					MessageSeverity.ERROR, HttpStatus.BAD_REQUEST, Arrays.toString(jwtTokenRequiredParameterList));
		}
		return person;
	}

	public static boolean isPersonTraitsValid(final PersonTraits person, final String[] jwtTokenRequiredParameterList) {
		if (jwtTokenRequiredParameterList == null) {
			return true;
		}

		boolean isValid = true;
		for (String parameter : jwtTokenRequiredParameterList) {
			isValid = checkIfEachParameterIsValid(person, parameter);
			// check if the parameter is invalid and return immediately
			if(!isValid) {
				return isValid;
			}
		}

		return isValid;
	}

	private static boolean checkIfEachParameterIsValid(final PersonTraits person, final String parameter) {
		for (Method method : PersonTraits.class.getMethods()) {
			if (method.getName().startsWith("get") && method.getName().substring(3).equalsIgnoreCase(parameter)) {
				try {
					return method.invoke(person) instanceof String ? StringUtils.isNotBlank((String) method.invoke(person))
							: method.invoke(person) != null;
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					vopLogger.error("Unable to check required fields in the jwt token", e);
					return false;
				}
			}
		}
		return false;
	}
}
