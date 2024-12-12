package nz.jive.hub.handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import nz.jive.hub.api.HomeResp;
import nz.jive.hub.api.MenuItem;
import nz.jive.hub.api.domain.User;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.facade.MenuFacade;
import nz.jive.hub.facade.SessionFacade;
import nz.jive.hub.service.security.Policy;
import org.jetbrains.annotations.NotNull;

import java.util.SortedSet;

/**
 * @author Goodie
 */
public class HomeHandler implements Handler {
    private final MenuFacade menuFacade;
    private final SessionFacade sessionFacade;

    public HomeHandler(MenuFacade menuFacade, SessionFacade sessionFacade) {
        this.menuFacade = menuFacade;
        this.sessionFacade = sessionFacade;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        //Object o = ctx.bodyAsClass(Object.class);
        OrganisationRecord organisation = sessionFacade.organisation(ctx).orElseThrow();
        Policy userPolicy = sessionFacade.policy(ctx).orElseThrow();

        SortedSet<MenuItem> menuItems = menuFacade.buildMenu(organisation, userPolicy);

        User user = sessionFacade.userRecord(ctx)
                .map(u -> new User(u.getId(), u.getName(), u.getPreferredName(), u.getEmail()))
                .orElse(null);

        ctx.json(new HomeResp(organisation.getDisplayName(), menuItems, user));
    }
}
