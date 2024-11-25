package nz.jive.hub.handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import nz.jive.hub.Parameters;
import nz.jive.hub.SecurityValues;
import nz.jive.hub.Server;
import nz.jive.hub.api.HomeResp;
import nz.jive.hub.api.MenuItem;
import nz.jive.hub.api.domain.User;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.database.generated.tables.records.UserDetailRecord;
import nz.jive.hub.service.PageService;
import nz.jive.hub.service.ParameterStoreService;
import nz.jive.hub.service.SecurityValidationService;
import nz.jive.hub.service.parameters.ParameterMap;
import nz.jive.hub.service.security.Policy;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Goodie
 */
public class HomeHandler implements Handler {
    private final SecurityValidationService securityValidationService;
    private final PageService pageService;
    private final ParameterStoreService parameterStoreService;

    public HomeHandler(SecurityValidationService securityValidationService,
                       PageService pageService,
                       ParameterStoreService parameterStoreService) {
        this.securityValidationService = securityValidationService;
        this.pageService = pageService;
        this.parameterStoreService = parameterStoreService;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        //Object o = ctx.bodyAsClass(Object.class);
        OrganisationRecord organisation = Objects.requireNonNull(ctx.attribute(Server.ATTR_ORGANISATION));
        Policy userPolicy = Objects.requireNonNull(ctx.attribute(Server.ATTR_USER_POLICY));
        UserDetailRecord userDetailRecord = ctx.attribute(Server.ATTR_USER);
        ParameterMap organizationParameters = parameterStoreService.getOrganizationParameters(organisation.getId());

        SortedSet<MenuItem> menuItems = new TreeSet<>(pageService.findMenuPages(organisation.getId()));
        if (organizationParameters.boolVal(Parameters.SCHOOL_ENABLED)) {
            menuItems.add(new MenuItem("School", "/school/", 95));

            if (securityValidationService.check(userPolicy, SecurityValues.SCHOOL_MANAGE_VIEW)) {
                menuItems.add(new MenuItem("School Manage", "/school/manage/", 96));
            }
        }

        if (organizationParameters.boolVal(Parameters.EVENT_ENABLED)) {
            menuItems.add(new MenuItem("Events", "/events/", 97));

            if (securityValidationService.check(userPolicy, SecurityValues.EVENT_MANAGE_VIEW)) {
                menuItems.add(new MenuItem("Events Manage", "/events/manage/", 98));
            }
        }

        if (securityValidationService.check(userPolicy, SecurityValues.ADMIN_VIEW)) {
            menuItems.add(new MenuItem("Admin", "/admin/", 99));
        }

        User user = Optional.ofNullable(userDetailRecord)
                .map(u -> new User(u.getId(), u.getName(), u.getPreferredName(), u.getEmail()))
                .orElse(null);

        ctx.json(new HomeResp(organisation.getDisplayName(), menuItems, user));
    }
}
