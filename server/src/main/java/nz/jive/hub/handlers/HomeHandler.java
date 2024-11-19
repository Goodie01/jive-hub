package nz.jive.hub.handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import nz.jive.hub.api.HomeResp;
import nz.jive.hub.api.MenuItem;
import nz.jive.hub.database.DatabaseService;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.database.generated.tables.records.UserDetailRecord;
import nz.jive.hub.service.PageService;
import nz.jive.hub.service.SecurityValidationService;
import nz.jive.hub.service.security.Policy;
import org.jetbrains.annotations.NotNull;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Goodie
 */
public class HomeHandler implements Handler {
    private final DatabaseService databaseService;
    private final SecurityValidationService securityValidationService;
    private final PageService pageService;

    public HomeHandler(DatabaseService databaseService, SecurityValidationService securityValidationService, PageService pageService) {
        this.databaseService = databaseService;
        this.securityValidationService = securityValidationService;
        this.pageService = pageService;
    }

    public static HomeHandler create(final DatabaseService databaseService, SecurityValidationService securityValidationService, PageService pageService) {
        return new HomeHandler(databaseService, securityValidationService, pageService);
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        //Object o = ctx.bodyAsClass(Object.class);
        OrganisationRecord organisation = ctx.attribute("organisation");
        UserDetailRecord user = ctx.attribute("user");
        Policy userPolicy = ctx.attribute("userPolicy");

        SortedSet<MenuItem> menuItems = new TreeSet<>(pageService.findMenuPages(organisation.getId()));

        if (securityValidationService.check(userPolicy, "admin.view")) {
            menuItems.add(new MenuItem("Admin", "/admin/"));
        }

        ctx.json(new HomeResp(organisation.getDisplayName(), menuItems));
    }
}
