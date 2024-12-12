package nz.jive.hub.handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import nz.jive.hub.api.LoginReq;
import nz.jive.hub.api.LoginResp;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.database.generated.tables.records.UserSessionRecord;
import nz.jive.hub.facade.SessionFacade;
import nz.jive.hub.facade.UserSessionFacade;
import org.jetbrains.annotations.NotNull;

/**
 * @author Goodie
 */
public class LoginHandler implements Handler {
    private final UserSessionFacade userSessionFacade;
    private final SessionFacade sessionFacade;

    public LoginHandler(UserSessionFacade userSessionFacade, SessionFacade sessionFacade) {
        this.userSessionFacade = userSessionFacade;
        this.sessionFacade = sessionFacade;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        LoginReq loginReq = ctx.bodyAsClass(LoginReq.class);

        OrganisationRecord organisation = sessionFacade.organisation(ctx).orElseThrow();

        UserSessionRecord userSessionRecord = userSessionFacade.createSession(organisation, loginReq.email());

        ctx.json(new LoginResp(userSessionRecord.getSessionKey()));
    }
}
