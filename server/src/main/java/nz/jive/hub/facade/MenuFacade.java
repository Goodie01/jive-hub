package nz.jive.hub.facade;

import nz.jive.hub.Parameters;
import nz.jive.hub.SecurityValues;
import nz.jive.hub.api.MenuItem;
import nz.jive.hub.database.DatabaseService;
import nz.jive.hub.database.Repository.PageRepository;
import nz.jive.hub.database.Repository.ParameterStoreRepository;
import nz.jive.hub.database.Repository.parameters.ParameterMap;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.service.server.ServerContext;
import org.jooq.Configuration;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Goodie
 */
public class MenuFacade {

    private final ParameterStoreRepository parameterStoreRepository;
    private final DatabaseService databaseService;
    private final PageRepository pageService;

    public MenuFacade(DatabaseService databaseService, ParameterStoreRepository parameterStoreRepository, PageRepository pageService) {
        this.parameterStoreRepository = parameterStoreRepository;
        this.databaseService = databaseService;
        this.pageService = pageService;
    }

    public SortedSet<MenuItem> buildMenu(ServerContext serverContext) {
        OrganisationRecord organisation = serverContext.organisation();
        Configuration configuration = databaseService.getConfiguration();
        ParameterMap organizationParameters = parameterStoreRepository.getOrganizationParameters(configuration, organisation.getId());

        SortedSet<MenuItem> menuItems = new TreeSet<>(pageService.findMenuPages(configuration, organisation.getId()));
        if (organizationParameters.boolVal(Parameters.SCHOOL_ENABLED)) {
            menuItems.add(new MenuItem("School", "/school/", 95));

            if (serverContext.check(SecurityValues.SCHOOL_MANAGE_VIEW)) {
                menuItems.add(new MenuItem("School Manage", "/school/manage/", 96));
            }
        }

        if (organizationParameters.boolVal(Parameters.EVENT_ENABLED)) {
            menuItems.add(new MenuItem("Events", "/events/", 97));

            if (serverContext.check(SecurityValues.EVENT_MANAGE_VIEW)) {
                menuItems.add(new MenuItem("Events Manage", "/events/manage/", 98));
            }
        }

        if (serverContext.check(SecurityValues.ADMIN_VIEW)) {
            menuItems.add(new MenuItem("Admin", "/admin/", 99));
        }

        return menuItems;
    }
}
