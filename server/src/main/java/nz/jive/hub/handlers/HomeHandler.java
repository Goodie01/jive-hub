package nz.jive.hub.handlers;

import io.javalin.http.Context;
import nz.jive.hub.api.HomeResp;
import nz.jive.hub.api.MenuItem;
import nz.jive.hub.api.domain.User;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.facade.MenuFacade;
import nz.jive.hub.facade.SessionFacade;
import nz.jive.hub.service.SecurityValidationService;
import nz.jive.hub.service.security.Policy;
import org.jetbrains.annotations.NotNull;

import java.util.SortedSet;

/**
 * @author Goodie
 */
public class HomeHandler implements InternalHandler {
    private final MenuFacade menuFacade;

    public HomeHandler(MenuFacade menuFacade) {
        this.menuFacade = menuFacade;
    }

    @Override
    public void handle(@NotNull Context ctx, SessionFacade sessionFacade, SecurityValidationService securityValidationService) throws Exception {
        //Object o = ctx.bodyAsClass(Object.class);
        OrganisationRecord organisation = sessionFacade.organisation().orElseThrow();
        Policy userPolicy = sessionFacade.policy().orElseThrow();

        SortedSet<MenuItem> menuItems = menuFacade.buildMenu(organisation, securityValidationService);

        User user = sessionFacade.userRecord()
                .map(u -> new User(u.getId(), u.getName(), u.getPreferredName(), u.getEmail()))
                .orElse(null);

        ctx.json(new HomeResp(organisation.getDisplayName(), menuItems, user));
    }
}
