package com.wynd.vop.framework.kong;

import com.wynd.vop.framework.client.rest.template.RestClientTemplate;
import com.wynd.vop.framework.log.BipLogger;
import com.wynd.vop.framework.log.BipLoggerFactory;
import com.wynd.vop.framework.security.jwt.JwtAuthenticationProperties.JwtKeyPairs;
import com.wynd.vop.framework.security.jwt.JwtSources;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wynd.vop.framework.kong.model.KongJwtTokenResult;
import com.wynd.vop.framework.kong.model.KongJwtToken;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Class used to fetch, parse and store secrets from Kong.
 */
public class KongConsumerService {

	private List<JwtKeyPairs> kongJwtKeyPairs;

	static final BipLogger LOGGER = BipLoggerFactory.getLogger(KongConsumerService.class);
	private RestClientTemplate restClientTemplate = new RestClientTemplate();

	/**
	 * Gets Kong Consumer Data and sets to KeyPair list
	 * 
	 * @param kongUri
	 * @param consumer
	 * @param adminConsumerKey
	 */
	public void getKongConsumerJwtData(String kongUri, String kongJwtResourcePath, String consumer,
			String adminConsumerKey) {

		try {

			kongJwtKeyPairs = new ArrayList<>();

			// MessageFormat.format(/path/consumer/{0}/jwt?apikey={1}, consumer,
			// adminConsumerKey)
			String uriPath = sanitizeKongUrl(kongUri)
					+ MessageFormat.format(kongJwtResourcePath, consumer, adminConsumerKey);

			LOGGER.debug("Get Consumer JWT: {}", uriPath);

			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

			ResponseEntity<String> responseEntity = getRestClientTemplate().executeURL(uriPath, HttpMethod.GET, request,
					new ParameterizedTypeReference<String>() {
					});

			if (responseEntity != null) {
				LOGGER.debug("[GET Kong Consumer JWT] Response Received, StatusCode: {}, Body: {}",
						responseEntity.getStatusCode().toString(), responseEntity.getBody());
			}

			if (responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()
					&& responseEntity.getBody() != null) {
				addKongKeyPairsFromResponse(getDeserializedKongJwtTokenResult(responseEntity.getBody()).getData());
			} else {
				LOGGER.warn("Failure or Empty Response from Kong for consumer: {}", consumer);
			}

		} catch (Exception ex) {
			LOGGER.error("Issue getting Kong properties from kong: {}", ex.getMessage());
		}

	}

	/**
	 * Adds Kong JWT KeyPair Results to the Kong KeyPairs list.
	 *
	 * @param kongJwtTokenList the Kong JWT Token List
	 */
	private void addKongKeyPairsFromResponse(List<KongJwtToken> kongJwtTokenList) {
		if (kongJwtTokenList != null && !kongJwtTokenList.isEmpty()) {
			for (KongJwtToken kongToken : kongJwtTokenList) {
				JwtKeyPairs keyPair = new JwtKeyPairs();
				keyPair.setIssuer(kongToken.getKey());
				keyPair.setSecret(kongToken.getSecret());
				keyPair.setSignatureAlgorithm(
						kongToken.getAlgorithm() != null ? SignatureAlgorithm.forName(kongToken.getAlgorithm()) : null);
				keyPair.setPublicKey(kongToken.getRsaPublicKey());
				keyPair.setSource(JwtSources.KONG.value());
				LOGGER.debug("Added a keypair [key]: " + kongToken.getKey());
				kongJwtKeyPairs.add(keyPair);
			}
		}
	}

	/**
	 * Deserialize json response from Kong API to KongJwtTokenResult object
	 * 
	 * @param jsonData
	 * @return KongJwtTokenResult
	 */
	public KongJwtTokenResult getDeserializedKongJwtTokenResult(String jsonData) {

		KongJwtTokenResult jwtTokenResult = null;

		try {

			LOGGER.debug("Deserializing json: " + jsonData);

			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

			jwtTokenResult = mapper.readValue(jsonData, KongJwtTokenResult.class);

		} catch (Exception ex) {
			LOGGER.error("Issue deserializing json:{}", ex.getMessage());
		}

		return jwtTokenResult;
	}

	/**
	 * Gets the kong jwt key pairs.
	 *
	 * @return the kong jwt key pairs
	 */
	public List<JwtKeyPairs> getKongJwtKeyPairs() {
		return kongJwtKeyPairs;
	}

	/**
	 * Sets the kong jwt key pairs.
	 *
	 * @param kongJwtKeyPairs the new kong jwt key pairs
	 */
	public void setKongJwtKeyPairs(List<JwtKeyPairs> kongJwtKeyPairs) {
		this.kongJwtKeyPairs = kongJwtKeyPairs;
	}

	/**
	 * Method to get RestClientTemplate - added/refactored for unit tests
	 * 
	 * @return RestClientTemplate
	 */
	public RestClientTemplate getRestClientTemplate() {
		return this.restClientTemplate;
	}

	/**
	 * Sets RestClientTemplate
	 * 
	 * @param restClientTemplate
	 */
	public void setRestClientTemplate(RestClientTemplate restClientTemplate) {
		this.restClientTemplate = restClientTemplate;
	}

	public String sanitizeKongUrl(String kongUrl) {
		if (kongUrl == null) {
			return "";
		}
		return kongUrl.endsWith("/") ? kongUrl : kongUrl + "/";
	}

}