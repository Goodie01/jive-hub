package nz.jive.hub.handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import nz.jive.hub.api.AdminQueryResp;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.facade.AdminFacade;
import nz.jive.hub.facade.SessionFacade;
import nz.jive.hub.service.security.Policy;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @author Goodie
 */
public class AdminQueryHandler implements Handler {
    private final AdminFacade adminFacade;
    private final SessionFacade sessionFacade;

    public AdminQueryHandler(AdminFacade adminFacade, SessionFacade sessionFacade) {
        this.adminFacade = adminFacade;
        this.sessionFacade = sessionFacade;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        OrganisationRecord organisation = sessionFacade.organisation(ctx).orElseThrow();
        Policy userPolicy = sessionFacade.policy(ctx).orElseThrow();

        Map<String, String> adminParameterValues = adminFacade.getValues(organisation, userPolicy);

        ctx.json(new AdminQueryResp(adminParameterValues));
    }
}
