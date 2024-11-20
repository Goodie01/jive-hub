package nz.jive.hub.handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import nz.jive.hub.Parameters;
import nz.jive.hub.SecurityValues;
import nz.jive.hub.api.HomeResp;
import nz.jive.hub.api.MenuItem;
import nz.jive.hub.database.DatabaseService;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.database.generated.tables.records.UserDetailRecord;
import nz.jive.hub.service.PageService;
import nz.jive.hub.service.ParameterStoreService;
import nz.jive.hub.service.SecurityValidationService;
import nz.jive.hub.service.parameters.ParameterMap;
import nz.jive.hub.service.security.Policy;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Goodie
 */
public class HomeHandler implements Handler {
    private final DatabaseService databaseService;
    private final SecurityValidationService securityValidationService;
    private final PageService pageService;
    private final ParameterStoreService parameterStoreService;

    public HomeHandler(DatabaseService databaseService,
                       SecurityValidationService securityValidationService,
                       PageService pageService,
                       ParameterStoreService parameterStoreService) {
        this.databaseService = databaseService;
        this.securityValidationService = securityValidationService;
        this.pageService = pageService;
        this.parameterStoreService = parameterStoreService;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        //Object o = ctx.bodyAsClass(Object.class);
        OrganisationRecord organisation = ctx.attribute("organisation");
        Policy userPolicy = ctx.attribute("userPolicy");
        ParameterMap organizationParameters = parameterStoreService.getOrganizationParameters(organisation.getId());

        Objects.requireNonNull(organisation);
        Objects.requireNonNull(userPolicy);

        //possibly null
        UserDetailRecord user = ctx.attribute("user");

        SortedSet<MenuItem> menuItems = new TreeSet<>(pageService.findMenuPages(organisation.getId()));

        if (securityValidationService.check(userPolicy, SecurityValues.ADMIN_VIEW)) {
            menuItems.add(new MenuItem("Admin", "/admin/"));
        }
        if (organizationParameters.boolVal(Parameters.SCHOOL_ENABLED)) {
            menuItems.add(new MenuItem("School", "/school/"));

            if (securityValidationService.check(userPolicy, SecurityValues.SCHOOL_MANAGE_VIEW)) {
                menuItems.add(new MenuItem("School Manage", "/school/manage/"));
            }
        }
        if (organizationParameters.boolVal(Parameters.EVENT_ENABLED)) {
            menuItems.add(new MenuItem("Events", "/events/"));

            if (securityValidationService.check(userPolicy, SecurityValues.EVENT_MANAGE_VIEW)) {
                menuItems.add(new MenuItem("Events Manage", "/events/manage/"));
            }
        }

        ctx.json(new HomeResp(organisation.getDisplayName(), menuItems));
    }
}
