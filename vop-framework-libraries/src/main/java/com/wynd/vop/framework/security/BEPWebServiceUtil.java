package com.wynd.vop.framework.security;

import com.wynd.vop.framework.log.BipLogger;
import com.wynd.vop.framework.log.BipLoggerFactory;
import com.wynd.vop.framework.shared.sanitize.Sanitizer;
import com.wynd.vop.framework.validation.Defense;
import com.wynd.vop.framework.util.HashGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * Provides a common method of handling elements of web service calls made to VA
 * Business Enterprise Platform (BEP) layer.
 */
public final class BEPWebServiceUtil {

	private static final Pattern IPv4RegexPattern = Pattern
			.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

	/** The Constant EXTERNALUID_LENGTH. */
	public static final int EXTERNALUID_LENGTH = 39;

	/** The Constant LOGGER. */
	private static final BipLogger LOGGER = BipLoggerFactory.getLogger(BEPWebServiceUtil.class);

	/**
	 * Private constructor to prevent instantiation.
	 */
	private BEPWebServiceUtil() {
	}

	/**
	 * Gets the External UID based on the below BEP specification. <br/>
	 * User’s identifier (user name, user id, etc…) corresponding to the
	 * caller’s identity in the client application. Do not use this element for
	 * internal application. 39 characters is the limit. <br/>
	 * The function will get the User ID (name) as recorded in the EVSS User
	 * table. If the ID is null, then the supplied default is used. If the ID
	 * meets the specification, then it is returned as-is. Else, the ID is
	 * converted to an MD5 digest.
	 *
	 * @param defaultVal
	 *            the default val
	 * @return the external uid
	 */
	public static String getExternalUID(final String defaultVal) {
		String computedVal = getComputedValue(SecurityUtils.getUserId(), defaultVal);

		if (computedVal.length() > EXTERNALUID_LENGTH) {
			computedVal = HashGenerator.getMd5ForString(computedVal);
		}

		Defense.notNull(computedVal);
		if (computedVal != null) {
			Assert.isTrue(computedVal.length() <= EXTERNALUID_LENGTH,
					"[Assertion failed] - this expression must be true");
		}
		return computedVal;
	}

	/**
	 * Gets the External Key based on the below BEP specification. <br/>
	 * User’s system identifier associated with the caller's identity (if the
	 * ExternalUid represents a unique identifier of the user’s identity, then
	 * this value is equal to the ExternalUid, otherwise represents a unique id
	 * – such a PK). Do not user this element for internal applications. 39
	 * characters is the limit. <br/>
	 *
	 * This function simply proxies to getExternalUid for now.
	 *
	 * @param defaultVal
	 *            the default val
	 * @return the external key
	 */
	public static String getExternalKey(final String defaultVal) {
		return getExternalUID(defaultVal);
	}

	/**
	 * Gets the client machine value. BEP specification - User’s workstation IP
	 * address.
	 *
	 * @param defaultValue the default value
	 * @return the client machine
	 */
	public static String getClientMachine(final String defaultValue) {
		return getClientMachine(defaultValue, true);
	}

	/**
	 * Gets the client machine value. BEP specification - User’s workstation IP
	 * address.
	 *
	 * @param defaultValue the default value
	 * @param useRequestAttributes whether to use the attributes from the RequestContextHolder to retrieve the client machine
	 * @return the client machine
	 */
	public static String getClientMachine(final String defaultValue, final boolean useRequestAttributes) {

		if (useRequestAttributes) {
		    LOGGER.debug("Retrieving Client Machine from Request Attributes.");
			final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
			if (requestAttributes instanceof ServletRequestAttributes) {
				final ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
				final HttpServletRequest servletRequest = servletRequestAttributes.getRequest();
				return servletRequest.getRemoteAddr();
			}
		}

		// Holds final computed value
		String computedVal = Sanitizer.stripXss(defaultValue);

		if (StringUtils.isBlank(defaultValue)) {
			try {
				computedVal = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				LOGGER.warn(e.getMessage(), e);
				computedVal = "localhost";
			}
		}

		try {
			for (Enumeration<NetworkInterface> enNetI = NetworkInterface
					.getNetworkInterfaces(); enNetI.hasMoreElements();) {
				NetworkInterface netI = enNetI.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = netI
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					computedVal = computeClientIP(inetAddress, computedVal);
				}
			}
		} catch (final SocketException e) {
			logError(e);
			if (StringUtils.isBlank(computedVal)) {
				computedVal = "UNKNOWN";
			}
		}
		return Sanitizer.stripXss(computedVal);
	}

	static void logError(final Exception e) {
		LOGGER.error(e.getMessage(), e);
	}

	/**
	 * Compute client IP.
	 *
	 * @param inetAddress the inet address
	 * @param defaultValue the default value
	 * @return the string
	 */
	private static String computeClientIP(final InetAddress inetAddress, final String defaultValue) {
		String computedVal = null;
		String hostAddress = Sanitizer.stripXss(inetAddress.getHostAddress());

		if (!inetAddress.isLoopbackAddress() && !inetAddress.isAnyLocalAddress()
				&& !inetAddress.isLinkLocalAddress() && IPv4RegexPattern.matcher(hostAddress).matches()) {
			computedVal = hostAddress;
		}
		return getComputedValue(computedVal, defaultValue);
	}

	/**
	 * Helper method that adds null checking to cumputedVal and defaultVal.
	 *
	 * @param computedVal
	 *            the computed val
	 * @param defaultVal
	 *            the default val
	 * @return the computed value
	 */
	private static String getComputedValue(final String computedVal, final String defaultVal) {
		String returnVal = computedVal;
		if (returnVal == null) {
			returnVal = defaultVal;
		}
		Defense.notNull(returnVal);
		return returnVal;
	}

}
