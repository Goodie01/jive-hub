package nz.jive.hub.handlers;

import io.javalin.http.Context;
import nz.jive.hub.api.LoginReq;
import nz.jive.hub.api.LoginResp;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.database.generated.tables.records.UserSessionRecord;
import nz.jive.hub.facade.SessionFacade;
import nz.jive.hub.facade.UserSessionFacade;
import nz.jive.hub.service.SecurityValidationService;
import nz.jive.hub.service.server.ServerContext;

import org.jetbrains.annotations.NotNull;

/**
 * @author Goodie
 */
public class LoginHandler implements InternalHandler {
    private final UserSessionFacade userSessionFacade;

    public LoginHandler(UserSessionFacade userSessionFacade) {
        this.userSessionFacade = userSessionFacade;
    }

    @Override
    public void handle(ServerContext serverContext) throws Exception {
        LoginReq loginReq = serverContext.getCtx().bodyAsClass(LoginReq.class);

        OrganisationRecord organisation = serverContext.organisation();

        UserSessionRecord userSessionRecord = userSessionFacade.createSession(organisation, loginReq.email());

        serverContext.getCtx().json(new LoginResp(userSessionRecord.getSessionKey()));
    }
}
