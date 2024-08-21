package com.wynd.vop.framework.security.user;

import lombok.Builder;
import lombok.Data;

/**
 * A representation of a user for BGS Service Calls.
 *
 * @since 3.1.0
 */
@Data
@Builder
public class VaUser {
    /**
     * The CSS Application Name for the user.
     */
    private String applicationName;
    /**
     * The username of the user.
     */
    private String username;
    /**
     * The ip address of the originating request by the user.
     */
    private String clientMachine;
    /**
     * The station where the user logged in from.
     */
    private Integer stationId;
    /**
     * The user identifier reserved for external operations to pass unique identity, particularly SOAP services.
     */
    private String externalUserId;
}
