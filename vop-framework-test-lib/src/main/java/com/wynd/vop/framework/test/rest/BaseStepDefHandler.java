package com.wynd.vop.framework.test.rest;

import com.wynd.vop.framework.test.util.RESTUtil;
import com.wynd.vop.framework.test.service.RESTConfigService;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handler object that extends BaseStepDef to handle rest based api call. Step
 * definition class inject this object thru constructor.
 *
 */
public class BaseStepDefHandler extends BaseStepDef {
	/**
	 * Constructor that calls initREST method of base class
	 */
	public BaseStepDefHandler() {
		initREST();
	}

	/**
	 * Constructor that calls initREST method of base class
	 * with custom HttpMessageConverter
	 *
	 * @param messageConverters
	 *            the message converters
	 */
	public BaseStepDefHandler(List<HttpMessageConverter<?>> messageConverters) {
		initREST(messageConverters);
	}

	/**
	 * Getter for RESTUtil.
	 *
	 * @return the rest util
	 */
	public RESTUtil getRestUtil() {
		return resUtil;
	}

	@Override
	/**
	 * Initialize header map with given key/value pair
	 */
	public void passHeaderInformation(Map<String, String> tblHeader) {
		headerMap = new HashMap<>(tblHeader);
	}

	/**
	 * Getter for header map.
	 *
	 * @return the header map
	 */
	public Map<String, String> getHeaderMap() {
		return headerMap;
	}

	/**
	 * Getter for returning API call response as string.
	 *
	 * @return the str response
	 */
	public String getStrResponse() {
		return strResponse;
	}

	/**
	 * Getter for returning API call response as object.
	 *
	 * @return the object response
	 */
	public Object getObjResponse() {
		return objResponse;
	}

	/**
	 * Getter for RESTConfigService.
	 *
	 * @return the rest config
	 */
	public RESTConfigService getRestConfig() {
		return restConfig;
	}

}
