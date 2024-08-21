package com.wynd.vop.framework.security.jwt;

import com.wynd.vop.framework.exception.BipRuntimeException;
import com.wynd.vop.framework.log.BipLogger;
import com.wynd.vop.framework.log.BipLoggerFactory;
import com.wynd.vop.framework.messages.MessageKeys;
import com.wynd.vop.framework.messages.MessageSeverity;
import com.wynd.vop.framework.security.PersonTraits;
import com.wynd.vop.framework.security.jwt.JwtAuthenticationProperties.JwtKeyPairs;
import com.wynd.vop.framework.security.jwt.correlation.CorrelationIdsParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.apache.ws.security.util.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Parse the encrypted JWT
 */
public class JwtParser {
	static final BipLogger LOGGER = BipLoggerFactory.getLogger(JwtParser.class);

	/** number of milliseconds in a second */
	private static final double NUMBER_OF_MILLIS_N_A_SECOND = 1000.0;

	/** The spring configurable properties used for authentication */
	private JwtAuthenticationProperties jwtAuthenticationProperties;

	/** The mappedAuthority claim to grab authorities information out of */
	private String mappedAuthority;

	/**
	 * Parse the JWT JSON into its component values
	 *
	 * @param properties
	 */
	public JwtParser(final JwtAuthenticationProperties properties) {
		this.jwtAuthenticationProperties = properties;
		this.mappedAuthority = properties.getValidation() == null ? null : properties.getValidation().getMappedAuthority();
	}

	/**
	 * Decrypts the JWT and attempts to construct a PersonTraits object from it.
	 * If correlation id parsing fails, {@code null} is returned.
	 *
	 * @param token
	 *            the encrypted JWT
	 * @return PersonTraits, or {@code null} if some issue with the correlation
	 *         ids
	 */
	public PersonTraits parseJwt(final String token) {

		long startTime = System.currentTimeMillis();

		Claims claims = null;
		claims = parseJwtKeyPairs(token);

		// Old feature to support single consumer
		if (claims == null && StringUtils.isNotBlank(jwtAuthenticationProperties.getIssuer())
					&& StringUtils.isNotBlank(jwtAuthenticationProperties.getSecret())) {
			LOGGER.debug("using old JWT single consumer");
			// We will sign our JWT with our ApiKey secret
			final Key signingKey = createSigningKey(jwtAuthenticationProperties.getSecret(), "", SignatureAlgorithm.HS256);

			claims = Jwts.parser().setSigningKey(signingKey).requireIssuer(jwtAuthenticationProperties.getIssuer())
					.parseClaimsJws(token).getBody();
		}

		final long elapsedTime = System.currentTimeMillis() - startTime;

		LOGGER.debug("Time elapsed to parse JWT token {}{}{}", "[", elapsedTime / NUMBER_OF_MILLIS_N_A_SECOND,
				" secs]");

		if (claims != null) {
			return getPersonFrom(claims);
		} else {
			return null;
		}
	}

	/**
	 * Parses the JWT key pairs.
	 *
	 * @param token
	 *            the token
	 * @return the claims
	 */
	private Claims parseJwtKeyPairs(final String token) {

		Claims claims = null;
		List<JwtKeyPairs> jwtKeyPairs =  jwtAuthenticationProperties.getKeyPairs();

		if (jwtKeyPairs != null && !jwtKeyPairs.isEmpty()) {
			for (int i = 0; i < jwtKeyPairs.size(); i++) {
				final JwtKeyPairs jwtKeyPair = jwtKeyPairs.get(i);
				try {
					// We will sign our JWT with our ApiKey secret
					Key signingKey = createSigningKey(jwtKeyPair.getSecret(), jwtKeyPair.getPublicKey(), jwtKeyPair.getSignatureAlgorithm());

					claims = Jwts.parser().setSigningKey(signingKey).requireIssuer(jwtKeyPair.getIssuer())
							.parseClaimsJws(token).getBody();
					LOGGER.trace("claims {}", claims);
				} catch (final Exception e) {
					LOGGER.debug("Exception parsing JWT token {}", e);
					if (i == jwtKeyPairs.size() - 1) {
						LOGGER.error("Failed after testing all known JWT tokens");
						throw e;
					} else {
						LOGGER.debug("Trying next JWT Key Pair");
					}
				} // end catch
				if (claims != null) {
					break;
				}
			}
		}
		return claims;
	}


	/**
	 * Creates the signing key.
	 *
	 * @param secret
	 *            the secret
	 * @param publicKey
     *            the public key
     * @return the key
	 */
	private Key createSigningKey(final String secret, final String publicKey, final SignatureAlgorithm signatureAlgorithm) {
		// The JWT signature algorithm we will be using to sign the token
        try {
            if (signatureAlgorithm == SignatureAlgorithm.RS256) {
                String trimmedPublicKey = publicKey.replace("-----BEGIN PUBLIC KEY-----", "")
                        .replace("-----END PUBLIC KEY-----", "")
                        .replace("\\ ", "");
    
                return KeyFactory.getInstance("RSA").
                        generatePublic(new X509EncodedKeySpec(Base64
                                .decode(trimmedPublicKey)));
            } else {
                SignatureAlgorithm sigAlg = signatureAlgorithm == null ? SignatureAlgorithm.HS256 : signatureAlgorithm;
                return new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),  sigAlg.getJcaName());
            }
        } catch (Exception e) {
            throw new BipRuntimeException(MessageKeys.VOP_SECURITY_JWT_CREATE_SIGNING_KEY_FAIL, MessageSeverity.ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
	}

	/**
	 * Attempts to produce a PersonTraits object from the correlation ids. If
	 * correlation id parsing fails, {@code null} is returned.
	 *
	 * @param claims
	 *            - the JWT contents
	 * @return PersonTraits, or {@code null} if some issue with the correlation
	 *         ids
	 */
	@SuppressWarnings("unchecked")
	private PersonTraits getPersonFrom(final Claims claims) {
		PersonTraits personTraits = null;
		if(mappedAuthority != null){
			Collection<GrantedAuthority> authorities = new ArrayList<>();
			Object mappedValue = claims.get(mappedAuthority);
			if (mappedValue instanceof String){
				authorities.add(new SimpleGrantedAuthority((String)mappedValue));
			} else if (mappedValue instanceof List){
				List<String> mappedValues = (List<String>) mappedValue;
				if (!mappedValues.isEmpty()) {
					for (final String value : mappedValues) {
						authorities.add(new SimpleGrantedAuthority(value));
					}
				}
			}
			personTraits = new PersonTraits(authorities);
		} else {
			personTraits = new PersonTraits();
		}

		personTraits.setClaims(claims);
		personTraits.setFirstName(claims.get("firstName", String.class));
		personTraits.setLastName(claims.get("lastName", String.class));
		personTraits.setPrefix(claims.get("prefix", String.class));
		personTraits.setMiddleName(claims.get("middleName", String.class));
		personTraits.setSuffix(claims.get("suffix", String.class));
		personTraits.setBirthDate(claims.get("birthDate", String.class));
		personTraits.setGender(claims.get("gender", String.class));
		personTraits.setAssuranceLevel(claims.get("assuranceLevel", Integer.class));
		personTraits.setEmail(claims.get("email", String.class));
		personTraits.setTokenId(claims.get("jti", String.class));
		personTraits.setAppToken(claims.get("appToken", String.class));
		personTraits.setApplicationID(claims.get("applicationID", String.class));
		personTraits.setUserID(claims.get("userID", String.class));
		personTraits.setStationID(claims.get("stationID", String.class));

		try {
			List<String> list = (List<String>) claims.get("correlationIds");
			CorrelationIdsParser.parseCorrelationIds(list, personTraits);

		} catch (final Exception e) { // NOSONAR intentionally wide, errors are
			// already logged
			// if there is any detected issue with the correlation ids
			personTraits = null;
		}

		return personTraits;
	}
}
