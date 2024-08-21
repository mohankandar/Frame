package com.wynd.vop.framework.security.util;

import com.wynd.vop.framework.exception.BipRuntimeException;
import com.wynd.vop.framework.messages.MessageKeys;
import com.wynd.vop.framework.messages.MessageSeverity;
import com.wynd.vop.framework.security.jwt.JwtAuthenticationProvider;
import com.wynd.vop.framework.security.model.Person;
import com.wynd.vop.framework.security.PersonTraits;
import com.wynd.vop.framework.security.jwt.JwtAuthenticationException;

import com.wynd.vop.framework.security.jwt.correlation.CorrelationIdsParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.ws.security.util.Base64;
import org.springframework.http.HttpStatus;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;


import java.util.*;

/**
 * Created by vgadda on 5/5/17.
 */
public class GenerateToken {

	private static String secret = "secret";
	private static String issuer = "Vets.gov";

	/**
	 * Do not instantiate
	 */
	private GenerateToken() {
	}

	public static String generateJwt() {
		return generateJwt(person(), 900, secret, issuer, null);
	}

	public static String generateJwt(final Person person, final String secret, final String issuer) {
		return generateJwt(person, 900, secret, issuer, null);
	}

	public static String generateJwt(final Person person) {
		return generateJwt(person, 900, secret, issuer, null);
	}

	public static String generateJwt(final int expireInsec) {
		return generateJwt(person(), expireInsec, secret, issuer, null);
	}

	public static String generateJwt(final Person person, final int expireInsec, final String secret, final String issuer,
			final String[] jwtTokenRequiredParameterList) {
        return generateJwt(person, expireInsec, secret, issuer, jwtTokenRequiredParameterList, SignatureAlgorithm.HS256);
	}
    
    public static String generateJwt(final Person person, final int expireInsec, final String secret, final String issuer,
                                     final String[] jwtTokenRequiredParameterList, final SignatureAlgorithm signatureAlgorithm) {

		final Calendar currentTime = GregorianCalendar.getInstance(Locale.US);
		Calendar expiration = GregorianCalendar.getInstance(Locale.US);
		expiration.setTime(currentTime.getTime());
		expiration.add(Calendar.SECOND, expireInsec);

        Key signingKey;
        // We will sign our JWT with our ApiKey secret
        if (signatureAlgorithm == SignatureAlgorithm.RS256){
            String trimmedSecret = secret.replace("-----BEGIN RSA PRIVATE KEY-----", "")
                    .replace("-----END RSA PRIVATE KEY-----", "")
                    .replace("\\ ", "");
            try {
                signingKey = KeyFactory.getInstance("RSA").
                        generatePrivate(new PKCS8EncodedKeySpec(Base64
                                .decode(trimmedSecret)));
            } catch (Exception e) {
                throw new BipRuntimeException(MessageKeys.VOP_SECURITY_JWT_CREATE_SIGNING_KEY_FAIL, MessageSeverity.ERROR,
                        HttpStatus.INTERNAL_SERVER_ERROR, e);
            }
        } else {
            signingKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), signatureAlgorithm.getJcaName());
        }
    
        final PersonTraits personTraits = populatePersonTraits(person);
        try {
            List<String> list = person.getCorrelationIds();
            CorrelationIdsParser.parseCorrelationIds(list, personTraits);
        
        } catch (Exception e) { // NOSONAR intentionally wide, errors are already logged
            // if there is any detected issue with the correlation ids
            throw new JwtAuthenticationException(MessageKeys.VOP_SECURITY_TOKEN_INVALID,
                    MessageSeverity.ERROR, HttpStatus.BAD_REQUEST);
        }
    
        if (!JwtAuthenticationProvider.isPersonTraitsValid(personTraits, jwtTokenRequiredParameterList)) {
            throw new JwtAuthenticationException(MessageKeys.VOP_SECURITY_TOKEN_INVALID_REQ_PARAM_MISSING,
                    MessageSeverity.ERROR, HttpStatus.BAD_REQUEST, Arrays.toString(jwtTokenRequiredParameterList));
        }
    
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setIssuer(issuer)
                .setIssuedAt(currentTime.getTime())
                .setId(UUID.randomUUID().toString())
                .setExpiration(expiration.getTime())
                .claim("firstName", person.getFirstName())
                .claim("middleName", person.getMiddleName())
                .claim("lastName", person.getLastName())
                .claim("prefix", person.getPrefix())
                .claim("suffix", person.getSuffix())
                .claim("birthDate", person.getBirthDate())
                .claim("gender", person.getGender())
                .claim("assuranceLevel", person.getAssuranceLevel())
                .claim("email", person.getEmail())
                .claim("correlationIds", person.getCorrelationIds())
                .claim("appToken", person.getAppToken())
                .claim("applicationID", person.getApplicationID())
                .claim("stationID", person.getStationID())
                .claim("userID", person.getUserID())
                .signWith(signatureAlgorithm, signingKey).compact();
    }

	private static PersonTraits populatePersonTraits(final Person person) {
		PersonTraits personTraits = new PersonTraits();

		personTraits.setFirstName(person.getFirstName());
		personTraits.setLastName(person.getLastName());
		personTraits.setPrefix(person.getPrefix());
		personTraits.setMiddleName(person.getMiddleName());
		personTraits.setSuffix(person.getSuffix());
		personTraits.setBirthDate(person.getBirthDate());
		personTraits.setGender(person.getGender());
		personTraits.setAssuranceLevel(person.getAssuranceLevel());
		personTraits.setEmail(person.getEmail());
		personTraits.setAppToken(person.getAppToken());
		personTraits.setApplicationID(person.getApplicationID());
		personTraits.setStationID(person.getStationID());
		personTraits.setUserID(person.getUserID());

		return personTraits;
	}

	public static Person person() {
		final Person person = new Person();
		person.setFirstName("JANE");
		person.setLastName("DOE");
		person.setPrefix("Ms");
		person.setMiddleName("M");
		person.setSuffix("S");
		person.setBirthDate("1955-01-01");
		person.setGender("FEMALE");
		person.setAssuranceLevel(2);
		person.setEmail("jane.doe@va.gov");

		final List<String> strArray = Arrays.asList("77779102^NI^200M^USVHA^P", "912444689^PI^200BRLS^USVBA^A",
				"6666345^PI^200CORP^USVBA^A", "1105051936^NI^200DOD^USDOD^A", "912444689^SS");
		person.setCorrelationIds(strArray);
		person.setAppToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9");
		person.setApplicationID("ShareUI");
		person.setStationID("310");
		person.setUserID("vhaislXXXXX");
		return person;
	}
}
