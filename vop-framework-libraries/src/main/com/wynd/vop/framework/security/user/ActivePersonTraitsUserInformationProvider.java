package com.wynd.vop.framework.security.user;

import com.wynd.vop.framework.exception.BipRuntimeException;
import com.wynd.vop.framework.messages.MessageKeys;
import com.wynd.vop.framework.messages.MessageSeverity;
import com.wynd.vop.framework.security.PersonTraits;
import com.wynd.vop.framework.security.SecurityUtils;
import com.wynd.vop.framework.security.model.AbstractPersonTraitsObject;
import org.springframework.http.HttpStatus;

/**
 * A {@link UserInformationProvider} implementation that retrieves the {@link PersonTraits} information
 * using the {@link SecurityUtils#getPersonTraits()} method, retrieving the active
 * {@link AbstractPersonTraitsObject}.
 *
 * @author dhamilton
 * @see PersonTraits
 * @see SecurityUtils#getPersonTraits()
 * @since 3.1.0
 */
public class ActivePersonTraitsUserInformationProvider implements UserInformationProvider {
    private static final String NO_USER_ERR = "VA Service call cannot be completed as there is no authenticated user.";
    private static final String STATION_INVALID_ERR = "The user's station must be numeric.";

    @Override
    public VaUser getUser() {
        final PersonTraits personTraits
                = SecurityUtils.getPersonTraits();
        if (personTraits == null) {
            throw new BipRuntimeException(MessageKeys.VOP_SECURITY_TOKEN_INVALID,
                    MessageSeverity.ERROR,
                    HttpStatus.BAD_REQUEST,
                    NO_USER_ERR);
        }

        Integer stationID = null;
        if (personTraits.getStationID() != null) {
            try {
                stationID = Integer.parseInt(personTraits.getStationID());
            } catch(NumberFormatException nfe) {
                throw new BipRuntimeException(MessageKeys.VOP_SECURITY_TOKEN_INVALID,
                        MessageSeverity.ERROR,
                        HttpStatus.BAD_REQUEST,
                        nfe, STATION_INVALID_ERR);
            }
        }

        return VaUser.builder()
                .applicationName(personTraits.getApplicationID())
                .username(personTraits.getUserID())
                .externalUserId(personTraits.getUserID())
                .stationId(stationID)
                .build();
    }


}
