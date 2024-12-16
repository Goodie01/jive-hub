package nz.jive.hub;

import io.javalin.http.Context;
import nz.jive.hub.facade.OrganisationFacade;
import nz.jive.hub.facade.SecurityFacade;
import nz.jive.hub.facade.SessionFacade;
import nz.jive.hub.facade.UserSessionFacade;
import nz.jive.hub.service.security.Policy;

/**
 * @author Goodie
 */
public class Server {
    public static final String ATTR_ORGANISATION = "organisation";

    public static void setup(Context ctx, UserSessionFacade userSessionFacade, OrganisationFacade organisationFacade, SecurityFacade securityFacade) {
        String host = ctx.header("host");
        SessionFacade sessionFacade = new SessionFacade(ctx);

        organisationFacade.findFromHostName(host)
                .ifPresentOrElse(organisation -> {
                            sessionFacade.store(organisation);

                            String authHeader = ctx.header("authorization");
                            userSessionFacade.findFromSessionKey(organisation, authHeader)
                                    .ifPresentOrElse(userDetailRecord -> {
                                                sessionFacade.store(userDetailRecord);

                                                Policy combinedRoleForUser = securityFacade.getCombinedRoleForUser(userDetailRecord); //TODO I need the bloody config
                                                sessionFacade.store(combinedRoleForUser);
                                            },
                                            () -> {
                                                Policy combinedRoleForUser = securityFacade.getDenyAllPolicy();
                                                sessionFacade.store(combinedRoleForUser);
                                            });
                        },
                        () -> {
                            Policy combinedRoleForUser = securityFacade.getDenyAllPolicy();
                            sessionFacade.store(combinedRoleForUser);
                        });
    }
}
