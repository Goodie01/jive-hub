package nz.jive.hub.handlers;

import nz.jive.hub.api.HomeResp;
import nz.jive.hub.api.MenuItem;
import nz.jive.hub.api.domain.Event;
import nz.jive.hub.api.domain.User;
import nz.jive.hub.database.generated.tables.records.EventRecord;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.facade.EventsFacade;
import nz.jive.hub.facade.MenuFacade;
import nz.jive.hub.service.server.ServerContext;

import java.util.List;
import java.util.SortedSet;

/**
 * @author Goodie
 */
public class HomeHandler implements InternalHandler {
    private final MenuFacade menuFacade;
    private final EventsFacade eventsFacade;

    public HomeHandler(MenuFacade menuFacade, EventsFacade eventsFacade) {
        this.menuFacade = menuFacade;
        this.eventsFacade = eventsFacade;
    }

    @Override
    public void handle(ServerContext serverContext) throws Exception {
        OrganisationRecord organisation = serverContext.organisation();

        SortedSet<MenuItem> menuItems = menuFacade.buildMenu(serverContext);
        List<EventRecord> eventRecords = eventsFacade.findAll(serverContext);

        User user = serverContext.userRecord()
                .map(u -> new User(u.getId(), u.getName(), u.getPreferredName(), u.getEmail()))
                .orElse(null);

        List<Event> events = eventRecords.stream()
                .map(e -> new Event(e.getId(), e.getDisplayName(), e.getByLine(), e.getStartDate(), e.getEndDate()))
                .toList();

        serverContext.getCtx().json(new HomeResp(organisation.getDisplayName(), menuItems, user, events));
    }
}
