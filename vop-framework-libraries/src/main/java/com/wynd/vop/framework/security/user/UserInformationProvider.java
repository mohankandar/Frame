package com.wynd.vop.framework.security.user;

import com.wynd.vop.framework.security.VAServiceWss4jSecurityInterceptor;

/**
 * Generic interface for retrieving a {@link VaUser} for populating the header generated for a BGS service call in the
 * {@link VAServiceWss4jSecurityInterceptor}.
 *
 * @author dhamilton
 * @see VaUser
 * @see VAServiceWss4jSecurityInterceptor
 * @since 3.1.0
 */
@FunctionalInterface
public interface UserInformationProvider {

    /**
     * Returns the {@link VaUser} containing the user information for the
     * {@link VAServiceWss4jSecurityInterceptor}.
     *
     * @return a {@link VaUser} containing the user information
     *
     * @see VaUser
     */
    VaUser getUser();
}
