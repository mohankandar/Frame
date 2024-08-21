package com.wynd.vop.framework.security;

import com.wynd.vop.framework.exception.BipRuntimeException;
import com.wynd.vop.framework.messages.MessageSeverity;
import com.wynd.vop.framework.security.user.StaticUserInformationProvider;
import com.wynd.vop.framework.security.user.UserInformationProvider;
import com.wynd.vop.framework.security.user.VaUser;
import com.wynd.vop.framework.validation.Defense;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.util.WSSecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecuritySecurementException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.annotation.PostConstruct;

/**
 * Extension of the Wss4jSecurityInterceptor to inject the VA application headers into the WSS security element
 * 
 * <pre>
 * &lt;vaws:VaServiceHeaders xmlns:vaws="http://vbawebservices.vba.va.gov/vawss">
 * &lt;vaws:applicationName>VDC</vaws:applicationName> </vaws:VaServiceHeaders>
 * </pre>
 */
@SuppressWarnings("squid:S1133") //"Deprecated code should be removed"
public class VAServiceWss4jSecurityInterceptor extends Wss4jSecurityInterceptor {

	/** The namespace constant. */
	public static final String VA_NS = "http://vbawebservices.vba.va.gov/vawss";

	/** The prefix constant */
	public static final String VA_PREFIX = "vaws:";

	/** The headers node constant */
	public static final String VA_SERVICE_HEADERS = "VaServiceHeaders";

	/** The application name node constant */
	public static final String VA_APPLICATION_NAME = "applicationName";

	/** The Constant CLIENT_MACHINE. */
	public static final String CLIENT_MACHINE = "CLIENT_MACHINE";

	/** The Constant STN_ID. */
	public static final String STN_ID = "STN_ID";

	/** The Constant EXTERNAL_UID. */
	public static final String EXTERNAL_UID = "ExternalUid";

	/** The Constant EXTERNAL_KEY. */
	public static final String EXTERNAL_KEY = "ExternalKey";

	/** The Constant for when a userid cannot be obtained. */
	public static final String EXTERNAL_UID_DEFAULT = "EVSS";
	
	/** Whether to include the external VA SOAP Headers. Not required and potentially breaking for batch and internal endpoints **/
	private boolean includeExternalVAHeaders = true;

	/** Whether to use the current request's context attributes to get the client machine */
	private boolean useRequestAttributesForClientMachine = true;

	/**
	 * The {@link UserInformationProvider} to retrieve the user for which to perform requests.
	 */
	private UserInformationProvider userInformationProvider;

	@PostConstruct
	public void postConstruct() {
		Defense.notNull(userInformationProvider);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor#secureMessage(
	 * org.springframework.ws.soap.SoapMessage, org.springframework.ws.context.MessageContext)
	 */
	@Override
	public final void secureMessage(final SoapMessage soapMessage, final MessageContext messageContext) {
		final VaUser user = userInformationProvider.getUser();
		messageContext.setProperty("Wss4jSecurityInterceptor.securementUser", user.getUsername());

		super.secureMessage(soapMessage, messageContext);

		// Build the VA Header Element
		final Document soapDoc = soapMessage.getDocument();
		final Element vaHeader = soapDoc.createElementNS(VA_NS, VA_PREFIX + VA_SERVICE_HEADERS);

		final Element clientMachineHeader = soapDoc.createElementNS(VA_NS, VA_PREFIX + CLIENT_MACHINE);
		clientMachineHeader.setTextContent(BEPWebServiceUtil.getClientMachine(user.getClientMachine(), useRequestAttributesForClientMachine));
		vaHeader.appendChild(clientMachineHeader);

		final Element stationIdHeader = soapDoc.createElementNS(VA_NS, VA_PREFIX + STN_ID);
		stationIdHeader.setTextContent(user.getStationId() == null ? null : user.getStationId().toString());
		vaHeader.appendChild(stationIdHeader);

		final Element appNameHeader = soapDoc.createElementNS(VA_NS, VA_PREFIX + VA_APPLICATION_NAME);
		appNameHeader.setTextContent(user.getApplicationName());
		vaHeader.appendChild(appNameHeader);
		
		if (includeExternalVAHeaders) {
            final Element externalUidHeader = soapDoc.createElementNS(VA_NS, VA_PREFIX + EXTERNAL_UID);
            final String userId = BEPWebServiceUtil.getExternalUID(EXTERNAL_UID_DEFAULT);
            externalUidHeader.setTextContent(userId);
            vaHeader.appendChild(externalUidHeader);
            
            final Element externalKeyHeader = soapDoc.createElementNS(VA_NS, VA_PREFIX + EXTERNAL_KEY);
            externalKeyHeader.setTextContent(userId); // RRR: should be changed BEPWebServiceUtil.getExternalKey(null)
            vaHeader.appendChild(externalKeyHeader);
        }

		// Add the VA application id header
		Element secHeader;
		try {
			secHeader = WSSecurityUtil.findWsseSecurityHeaderBlock(soapDoc, soapDoc.getDocumentElement(), true);
			secHeader.appendChild(vaHeader);
			soapMessage.setDocument(soapDoc);
		} catch (final WSSecurityException ex) {
			throw new Wss4jSecuritySecurementException(ex.getMessage(), ex);
		}

	}

	/**
	 * Gets the {@link UserInformationProvider} for the Interceptor.
	 * @return the {@link UserInformationProvider} for the Interceptor.
	 */
	public UserInformationProvider getUserInformationProvider() {
		return userInformationProvider;
	}

	/**
	 * Sets the {@link UserInformationProvider} for the Interceptor.
	 * @param userInformationProvider
	 */
	public void setUserInformationProvider(UserInformationProvider userInformationProvider) {
		this.userInformationProvider = userInformationProvider;
	}

	/**
	 * Gets the client machine.
	 *
	 * @return the client machine
	 *
	 * @see VaUser#getClientMachine()
	 * @deprecated since 3.1.0 to support the use of {@link UserInformationProvider} and remove static users
	 */
	@Deprecated
	public final String getClientMachine() {
		return userInformationProvider.getUser().getClientMachine();
	}

	/**
	 * Sets the client machine.
	 *
	 * @param clientMachine the new client machine
	 *
	 * @see VaUser#setClientMachine(String)
	 * @deprecated since 3.1.0 to support the use of {@link UserInformationProvider} and remove static users
	 */
	@Deprecated
	public final void setClientMachine(final String clientMachine) {
		if (userInformationProvider instanceof StaticUserInformationProvider) {
			userInformationProvider.getUser().setClientMachine(clientMachine);
		} else {
			throw new UnsupportedOperationException("Cannot set clientMachine of a non-static user");
		}
	}

	/**
	 * Gets the station id.
	 *
	 * @return the station id
	 *
	 * @see VaUser#getStationId()
	 * @deprecated since 3.1.0 to support the use of {@link UserInformationProvider} and remove static users
	 */
	@Deprecated
	public final String getStationId() {
		Integer stationId = userInformationProvider.getUser().getStationId();
		return stationId == null ? null : stationId.toString();
	}

	/**
	 * Sets the station id.
	 *
	 * @param stationId the new station id
	 *
	 * @see VaUser#setStationId(Integer)
	 * @deprecated since 3.1.0 to support the use of {@link UserInformationProvider} and remove static users
	 */
	@Deprecated
	public final void setStationId(final String stationId) {
		if (userInformationProvider instanceof StaticUserInformationProvider) {
			try {
				userInformationProvider.getUser().setStationId(Integer.parseInt(stationId));
			} catch (NumberFormatException nfe) {
				throw new BipRuntimeException(null,
						MessageSeverity.FATAL,
						HttpStatus.INTERNAL_SERVER_ERROR,
						"Station ID must be numeric.");
			}
		} else {
			throw new UnsupportedOperationException("Cannot set stationId of a non-static user");
		}
	}

	/**
	 * Gets the va application name.
	 *
	 * @return the va application name
	 *
	 * @see VaUser#getApplicationName()
	 * @deprecated since 3.1.0 to support the use of {@link UserInformationProvider} and remove static users
	 */
	@Deprecated
	public final String getVaApplicationName() {
		return userInformationProvider.getUser().getApplicationName();
	}

	/**
	 * Sets the va application name.
	 *
	 * @param vaApplicationName the new va application name
	 *
	 * @see VaUser#setApplicationName(String)
	 * @deprecated to support the use of {@link UserInformationProvider} and remove static users
	 */
	@Deprecated
	public final void setVaApplicationName(final String vaApplicationName) {
		if (userInformationProvider instanceof StaticUserInformationProvider) {
			userInformationProvider.getUser().setApplicationName(vaApplicationName);
		} else {
			throw new UnsupportedOperationException("Cannot set vaApplicationName of a non-static user");
		}
	}
    
    /**
     * Gets whether external VA headers should be included in the request.
     *
     * @return whether external VA headers should be included
     */
    public final boolean isIncludeExternalVAHeaders() {
        return includeExternalVAHeaders;
    }
    
    /**
     * Sets whether external VA headers should be included in the request.
     *
     * @param includeExternalVAHeaders whether external VA headers should be included
     */
    public final void setIncludeExternalVAHeaders(boolean includeExternalVAHeaders) {
        this.includeExternalVAHeaders = includeExternalVAHeaders;
    }

	/**
	 * Gets whether the client machine should be retrieved from the current request context.
	 *
	 * @return whether the client machine should be retrieved from the current request context.
	 */
	public boolean isUseRequestAttributesForClientMachine() {
		return useRequestAttributesForClientMachine;
	}

	/**
	 * Sets whether the client machine should be retrieved from the current request context.
	 * @param useRequestAttributesForClientMachine whether the client machine should be retrieved from the current request context.
	 */
	public void setUseRequestAttributesForClientMachine(boolean useRequestAttributesForClientMachine) {
		this.useRequestAttributesForClientMachine = useRequestAttributesForClientMachine;
	}
}
