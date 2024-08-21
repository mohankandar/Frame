package com.wynd.vop.framework.security.jwt;

import com.wynd.vop.framework.exception.BipRuntimeException;
import com.wynd.vop.framework.log.BipLogger;
import com.wynd.vop.framework.log.BipLoggerFactory;
import com.wynd.vop.framework.messages.MessageKeys;
import com.wynd.vop.framework.messages.MessageSeverity;
import com.wynd.vop.framework.security.model.Person;
import com.wynd.vop.framework.security.util.GenerateToken;
import com.wynd.vop.framework.kong.KongProperties;
import com.wynd.vop.framework.kong.KongTokenService;
import com.wynd.vop.framework.security.jwt.JwtAuthenticationProperties.JwtKeyPairs;
import com.wynd.vop.framework.swagger.SwaggerResponseMessages;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TokenResource implements SwaggerResponseMessages {

	private static final String API_OPERATION_VALUE = "Get JWT Token";
	private static final String API_OPERATION_NOTES = "Get a JWT bearer token with 'person' data. "
			+ "Include MVI correlationIds if required by the target API.";
	private static final String API_PARAM_GETTOKEN_PERSON = "Identity information for the authenticated user. "
			+ "CorrelationIds may be null or an empty array if the target API does not require it. "
			+ "Otherwise, correlationIds must be the list as retrieved from MVI:\n"
			+ "<table style=\"table-layout:auto;width:700px;text-align:left;background-color:#efefef;\">"
			+ "<tr><th>Common ID Name</th><th>Example ID</th><th>Type</th><th>Source</th><th>Issuer</th><th>Status</th><th </tr>"
			+ "<tr><td>Participant ID (PID)</td><td>12345678</td><td>PI</td><td>200CORP</td><td>USVBA</td><td>A</td></tr>"
			+ "<tr><td>File Number</td><td>123456789</td><td>PI</td><td>200BRLS</td><td>USVBA</td><td>A</td></tr>"
			+ "<tr><td>ICN</td><td>1234567890V123456</td><td>NI</td><td>200M</td><td>USVHA</td><td>A</td></tr>"
			+ "<tr><td>EDIPI / PNID</td><td>1234567890</td><td>NI</td><td>200DOD</td><td>USDOD</td><td>A</td></tr>"
			+ "<tr><td>SSN</td><td>123456789</td><td>SS</td><td></td><td></td><td></td></tr>" + "</table>";

	@Autowired
	private JwtAuthenticationProperties jwtAuthenticationProperties;

	@Autowired
	private KongProperties kongProperties;

	private static final BipLogger LOG = BipLoggerFactory.getLogger(TokenResource.class);

	@Value("${vop.framework.security.jwt.validation.required-parameters:}")
	private String[] jwtTokenRequiredParameterList;

	@PostMapping(value = { "/token"}, consumes = { MediaType.ALL_VALUE }, produces = {
			MediaType.ALL_VALUE })
	@ApiOperation(value = API_OPERATION_VALUE, notes = API_OPERATION_NOTES)
			@ApiResponses(value = {
				@ApiResponse(code = 200, message =  MESSAGE_200),
				@ApiResponse(code = 400, message = MESSAGE_400),
				@ApiResponse(code = 500, message = MESSAGE_500) })
	public String getToken(
			@ApiParam(value = API_PARAM_GETTOKEN_PERSON, required = true) @RequestBody final Person person) {

		List<JwtKeyPairs> jwtKeyPairs = null;

		if (kongProperties != null && kongProperties.getEnabled()) {
			KongTokenService kongTokenService = new KongTokenService(jwtAuthenticationProperties,kongProperties);
			jwtKeyPairs = kongTokenService.getKongKeyPairs();

			if (jwtKeyPairs !=null) {
				for (JwtKeyPairs jwt : jwtKeyPairs) {
					LOG.info("Key: " + jwt.getIssuer() + " Secret: " + jwt.getSecret());
				}
			}

		}
		
		if (jwtKeyPairs == null || jwtKeyPairs.isEmpty()) {
			jwtKeyPairs = jwtAuthenticationProperties.getKeyPairs();
		}

		if (jwtKeyPairs != null && !jwtKeyPairs.isEmpty()) {

			JwtKeyPairs keyPair = jwtKeyPairs.stream()
					.filter(jwtKeyPair ->
							(jwtKeyPair != null
									&& (jwtKeyPair.getSignatureAlgorithm() == null || jwtKeyPair.getSignatureAlgorithm() == SignatureAlgorithm.HS256)
									&& jwtKeyPair.getSecret() != null))
					.findFirst().orElseThrow(() -> new BipRuntimeException(MessageKeys.VOP_SECURITY_GENERATE_JWT_UNSUPPORTED_ALGORITHM,
							MessageSeverity.ERROR, HttpStatus.INTERNAL_SERVER_ERROR));

			LOG.info("Generating Token using Issuer: " + keyPair.getIssuer());

			return GenerateToken.generateJwt(person, jwtAuthenticationProperties.getExpireInSeconds(),
					keyPair.getSecret(), keyPair.getIssuer(), jwtTokenRequiredParameterList);
		}

		return GenerateToken.generateJwt(person, jwtAuthenticationProperties.getExpireInSeconds(),
				jwtAuthenticationProperties.getSecret(), jwtAuthenticationProperties.getIssuer(),
				jwtTokenRequiredParameterList);
	}

	/**
	 * Registers fields that should be allowed for data binding.
	 *
	 * @param binder
	 *            Spring-provided data binding context object.
	 */
	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		binder.setAllowedFields("birthDate", "firstName", "lastName", "middleName", "prefix", "suffix", "gender",
				"assuranceLevel", "email", "dodedipnid", "pnidType", "pnid", "pid", "icn", "fileNumber", "tokenId",
				"correlationIds");
	}
}
