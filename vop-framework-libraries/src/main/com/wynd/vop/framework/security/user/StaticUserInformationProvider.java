package com.wynd.vop.framework.security.user;

/**
 * A {@link UserInformationProvider} implementation that provides back an unchanging user-name and station-id in the
 * resulting {@link VaUser}.
 *
 * @author dhamilton
 * @since 3.1.0
 */
public class StaticUserInformationProvider implements UserInformationProvider {

    /**
     * Unchanging user.
     */
    private final VaUser vaUser;

    /**
     * Constructor requiring the Application Name, UserName, and StationId of the {@link VaUser}.
     *
     * @param applicationName
     *         the application name of the resulting {@link VaUser}
     * @param username
     *         the UserName of the resulting {@link VaUser}
     * @param stationId
     *         the StationId of the resulting {@link VaUser}
     */
    public StaticUserInformationProvider(final String applicationName, final String username, final Integer stationId) {
        vaUser = VaUser.builder()
                       .applicationName(applicationName)
                       .username(username)
                       .stationId(stationId)
                       .build();
    }

    @Override
    public VaUser getUser() {
        return vaUser;
    }
}
