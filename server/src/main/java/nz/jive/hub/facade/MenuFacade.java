package nz.jive.hub.facade;

import nz.jive.hub.Parameters;
import nz.jive.hub.Repository.PageRepository;
import nz.jive.hub.Repository.ParameterStoreRepository;
import nz.jive.hub.Repository.parameters.ParameterMap;
import nz.jive.hub.api.MenuItem;
import nz.jive.hub.database.DatabaseService;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.service.SecurityValidationService;
import nz.jive.hub.service.security.Policy;
import nz.jive.hub.service.security.SecurityValues;
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
    private final SecurityValidationService securityValidationService;

    public MenuFacade(DatabaseService databaseService, ParameterStoreRepository parameterStoreRepository, PageRepository pageService, SecurityValidationService securityValidationService) {
        this.parameterStoreRepository = parameterStoreRepository;
        this.databaseService = databaseService;
        this.pageService = pageService;
        this.securityValidationService = securityValidationService;
    }

    public SortedSet<MenuItem> buildMenu(OrganisationRecord organisation, Policy userPolicy) {
        Configuration configuration = databaseService.getConfiguration();
        ParameterMap organizationParameters = parameterStoreRepository.getOrganizationParameters(configuration, organisation.getId());

        SortedSet<MenuItem> menuItems = new TreeSet<>(pageService.findMenuPages(configuration, organisation.getId()));
        if (organizationParameters.boolVal(Parameters.SCHOOL_ENABLED)) {
            menuItems.add(new MenuItem("School", "/school/", 95));

            if (securityValidationService.check(userPolicy, organisation.getSlug() + SecurityValues.SCHOOL_MANAGE_VIEW)) {
                menuItems.add(new MenuItem("School Manage", "/school/manage/", 96));
            }
        }

        if (organizationParameters.boolVal(Parameters.EVENT_ENABLED)) {
            menuItems.add(new MenuItem("Events", "/events/", 97));

            if (securityValidationService.check(userPolicy, organisation.getSlug() + SecurityValues.EVENT_MANAGE_VIEW)) {
                menuItems.add(new MenuItem("Events Manage", "/events/manage/", 98));
            }
        }

        if (securityValidationService.check(userPolicy, organisation.getSlug() + SecurityValues.ADMIN_VIEW)) {
            menuItems.add(new MenuItem("Admin", "/admin/", 99));
        }

        return menuItems;
    }
}
