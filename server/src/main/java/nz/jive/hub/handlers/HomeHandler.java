package nz.jive.hub.handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import java.util.List;
import nz.jive.hub.api.HomeResp;
import nz.jive.hub.database.DatabaseService;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.database.generated.tables.records.UserDetailRecord;
import nz.jive.hub.service.SecurityValidationService;
import nz.jive.hub.service.security.Policy;
import org.jetbrains.annotations.NotNull;

/**
 * @author Goodie
 */
public class HomeHandler implements Handler {
    private final DatabaseService databaseService;
    private final SecurityValidationService securityValidationService;

    public HomeHandler(DatabaseService databaseService, SecurityValidationService securityValidationService) {
        this.databaseService = databaseService;
        this.securityValidationService = securityValidationService;
    }

    public static HomeHandler create(final DatabaseService databaseService, SecurityValidationService securityValidationService) {
        return new HomeHandler(databaseService, securityValidationService);
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        //Object o = ctx.bodyAsClass(Object.class);
        OrganisationRecord organisation = ctx.attribute("organisation");
        UserDetailRecord user = ctx.attribute("user");
        Policy userPolicy = ctx.attribute("userPolicy");

        securityValidationService.check(userPolicy, "admin.view");


        ctx.json(new HomeResp(organisation.getDisplayName(), List.of()));
    }
}
