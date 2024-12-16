package nz.jive.hub.handlers;

import io.javalin.http.Context;
import nz.jive.hub.api.AdminQueryResp;
import nz.jive.hub.api.ConfigurationValue;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.facade.AdminFacade;
import nz.jive.hub.facade.SessionFacade;
import nz.jive.hub.service.SecurityValidationService;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

/**
 * @author Goodie
 */
public class AdminQueryHandler implements InternalHandler {
    private final AdminFacade adminFacade;

    public AdminQueryHandler(AdminFacade adminFacade) {
        this.adminFacade = adminFacade;
    }

    @Override
    public void handle(@NotNull Context ctx, SessionFacade sessionFacade, SecurityValidationService securityValidationService) throws Exception {
        OrganisationRecord organisation = sessionFacade.organisation().orElseThrow();

        Set<ConfigurationValue> adminParameterValues = adminFacade.getValues(organisation, securityValidationService);

        ctx.json(new AdminQueryResp(adminParameterValues));
    }
}
