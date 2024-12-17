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
import nz.jive.hub.service.server.ServerContext;

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
    public void handle(ServerContext serverContext) throws Exception {
        OrganisationRecord organisation = serverContext.organisation();

        SortedSet<MenuItem> menuItems = menuFacade.buildMenu(serverContext);

        User user = serverContext.userRecord()
                .map(u -> new User(u.getId(), u.getName(), u.getPreferredName(), u.getEmail()))
                .orElse(null);

        serverContext.getCtx().json(new HomeResp(organisation.getDisplayName(), menuItems, user));
    }
}
