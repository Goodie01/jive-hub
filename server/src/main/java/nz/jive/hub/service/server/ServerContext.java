package nz.jive.hub.service.server;

import java.util.Optional;

import io.javalin.http.Context;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.database.generated.tables.records.UserDetailRecord;
import nz.jive.hub.facade.SessionFacade;
import nz.jive.hub.service.SecurityValidationService;
import nz.jive.hub.service.security.Policy;

/**
 * @author thomas.goodwin
 */
public class ServerContext {
    private final Context ctx;
    private final SessionFacade sessionFacade;
    private final SecurityValidationService securityValidationService;

    public ServerContext(Context ctx, SessionFacade sessionFacade, SecurityValidationService securityValidationService) {
        this.ctx = ctx;
        this.sessionFacade = sessionFacade;
        this.securityValidationService = securityValidationService;
    }

    public Context getCtx() {
        return ctx;
    }

    public boolean check(final String action) {
        return securityValidationService.check(action);
    }

    public void checkThrows(final String action) {
        securityValidationService.checkThrows(action);
    }

    public void checkThrows(final String action, final String resource) {
        securityValidationService.checkThrows(action, resource);
    }

    public boolean check(final String action, final String resource) {
        return securityValidationService.check(action, resource);
    }

    public Optional<UserDetailRecord> userRecord() {
        return sessionFacade.userRecord();
    }

    public Policy policy() {
        return sessionFacade.policy().orElseThrow();
    }

    public OrganisationRecord organisation() {
        return sessionFacade.organisation().orElseThrow();
    }

    public SecurityValidationService securityValidationService() {
        return securityValidationService;
    }
}
