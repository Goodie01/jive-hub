package nz.jive.hub.handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import nz.jive.hub.Server;
import nz.jive.hub.api.LoginReq;
import nz.jive.hub.api.LoginResp;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.database.generated.tables.records.UserDetailRecord;
import nz.jive.hub.database.generated.tables.records.UserSessionRecord;
import nz.jive.hub.service.UserService;
import nz.jive.hub.service.UserSessionService;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Goodie
 */
public class LoginHandler implements Handler {

    private final UserService userService;
    private final UserSessionService userSessionService;

    public LoginHandler(UserService userService, UserSessionService userSessionService) {
        this.userService = userService;
        this.userSessionService = userSessionService;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        LoginReq loginReq = ctx.bodyAsClass(LoginReq.class);

        OrganisationRecord organisation = Objects.requireNonNull(ctx.attribute(Server.ATTR_ORGANISATION));

        UserDetailRecord userDetailRecord = userService.findByEmail(organisation.getId(), loginReq.email()).orElseThrow();
        UserSessionRecord userSessionRecord = userSessionService.create(organisation.getId(), userDetailRecord.getId());

        ctx.json(new LoginResp(userSessionRecord.getSessionKey()));
    }
}
