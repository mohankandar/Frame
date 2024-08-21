package com.wynd.vop.framework.test.service;

import com.wynd.vop.framework.test.exception.BipTestLibRuntimeException;
import com.wynd.vop.framework.test.util.RESTUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * It Fetches token from the token API. The token will be used as a header while
 * invoking actual end points.
 * 
 * @author sravi
 *
 */

public class BearerTokenService {

	private static final String TOKEN_URL_PROPERTY_KEY = "tokenUrl";
	private static final String COULD_NOT_FIND_PROPERTY_STRING = "Could not find property : ";
	private static final String BASE_URL_PROPERTY_KEY = "baseURL";

	/**
	 * A service object that deals with bearer token. BearerTokenService fetch token
	 * before every API call.
	 */
	private static BearerTokenService instance = null;

	/**
	 * String that holds bearerToken
	 */
	private String bearerToken = "";

	/**
	 * Empty private constructor that should not initialized.
	 */
	private BearerTokenService() {

	}

	/**
	 * Function that initializes BearerTokenService as singleton object.
	 *
	 * @return single instance of BearerTokenService
	 */
	public static BearerTokenService getInstance() {

		if (instance == null) {
			instance = new BearerTokenService();
			instance.bearerToken = getToken("token.request");
		}
		return instance;
	}

	/**
	 * Invokes bearer token API with header information loaded from give filepath.
	 *
	 * @param headerFile the header file
	 * @return the token by header file
	 */
	public static String getTokenByHeaderFile(final String headerFile) {
		return getToken(headerFile);
	}

	/**
	 * Makes API call to bearer token service and returns the token as string.
	 *
	 * @param headerFile the header file
	 * @return the token
	 */
	public static String getToken(final String headerFile) {
		final RESTConfigService restConfig = RESTConfigService.getInstance();
		final String baseUrl = restConfig.getProperty(BASE_URL_PROPERTY_KEY, true);
		final String tokenUrl = restConfig.getProperty(TOKEN_URL_PROPERTY_KEY,true);
		handleNullUrls(baseUrl, tokenUrl);
		final Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Accept", ContentType.APPLICATION_JSON.getMimeType());
		headerMap.put("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
		RESTUtil restUtility = new RESTUtil(new ArrayList<>());
		restUtility.setUpRequest(headerFile, headerMap);
		return restUtility.postResponse(baseUrl + tokenUrl);
	}

	/**
	 * Handle null urls.
	 *
	 * @param baseUrl the base url
	 * @param tokenUrl the token url
	 */
	private static void handleNullUrls(final String baseUrl, final String tokenUrl) {
		if (StringUtils.isBlank(baseUrl) || StringUtils.isBlank(tokenUrl)) {
			String propertyNotFound = StringUtils.isBlank(baseUrl) ? BASE_URL_PROPERTY_KEY : "";
			if (StringUtils.isBlank(tokenUrl)) {
				if (StringUtils.isNotBlank(propertyNotFound)) {
					propertyNotFound = propertyNotFound + ", ";
				}
				propertyNotFound = propertyNotFound + TOKEN_URL_PROPERTY_KEY;
			}
			throw new BipTestLibRuntimeException(COULD_NOT_FIND_PROPERTY_STRING + propertyNotFound);
		}
	}

	/**
	 * Returns bearer token.
	 *
	 * @return the bearer token
	 */
	public String getBearerToken() {
		return bearerToken;
	}

}
