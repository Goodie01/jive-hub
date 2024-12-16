package nz.jive.hub.handlers;

import java.util.Set;

import org.jetbrains.annotations.NotNull;

import io.javalin.http.Context;
import nz.jive.hub.api.AdminQueryResp;
import nz.jive.hub.api.AdminUpdateReq;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.facade.AdminFacade;
import nz.jive.hub.facade.SessionFacade;
import nz.jive.hub.service.SecurityValidationService;

/**
 * @author thomas.goodwin
 */
public class AdminUpdateHandler implements InternalHandler {
    private final AdminFacade adminFacade;

    public AdminUpdateHandler(AdminFacade adminFacade) {
        this.adminFacade = adminFacade;
    }

    @Override
    public void handle(@NotNull Context ctx, SessionFacade sessionFacade, SecurityValidationService securityValidationService) throws Exception {
        AdminUpdateReq adminUpdateReq = ctx.bodyAsClass(AdminUpdateReq.class);
        OrganisationRecord organisation = sessionFacade.organisation().orElseThrow();

        adminFacade.updateValues(organisation, securityValidationService, adminUpdateReq);
        Set<AdminQueryResp.ConfigurationValue> adminParameterValues = adminFacade.getAllValues(organisation, securityValidationService);

        ctx.json(new AdminQueryResp(adminParameterValues));
    }
}
