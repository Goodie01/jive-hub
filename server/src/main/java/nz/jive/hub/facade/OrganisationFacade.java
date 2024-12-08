package nz.jive.hub.facade;

import nz.jive.hub.Parameters;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.database.generated.tables.records.UserDetailRecord;
import nz.jive.hub.service.*;
import nz.jive.hub.service.parameters.ParameterMap;
import nz.jive.hub.service.security.Policy;
import nz.jive.hub.service.security.Statement;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * @author thomas.goodwin
 */
public class OrganisationFacade {
    private static final Pattern DISPLAY_NAME_VALIDATOR = Pattern.compile("^([a-zA-Z0-9\\s-])+$");

    private final OrganisationService organisationService;
    private final PageService pageService;
    private final ParameterStoreService parameterStoreService;
    private final UserService userService;
    private final SecurityService securityService;

    public OrganisationFacade(final OrganisationService organisationService,
                              final PageService pageService,
                              final ParameterStoreService parameterStoreService,
                              final UserService userService, SecurityService securityService) {
        this.organisationService = organisationService;
        this.pageService = pageService;
        this.parameterStoreService = parameterStoreService;
        this.userService = userService;
        this.securityService = securityService;
    }

    public void createOrganisation(final String orgDisplayName,
                                   final String userName,
                                   final String userPreferredName,
                                   final String email) {

        if (!DISPLAY_NAME_VALIDATOR
                .matcher(orgDisplayName)
                .matches()) {
            throw new IllegalArgumentException(orgDisplayName + " is not a valid display name");
        }

        OrganisationRecord organisation = organisationService.createOrganisation(orgDisplayName);

        pageService.save(
                organisation.getId(), "",
                """
                        # #orgName
                        This is your Jive Hub home page!
                        """.replace("#orgName", orgDisplayName),
                orgDisplayName + " - Home",
                "Home", 0);

        UserDetailRecord userDetailRecord = userService.create(userName, userPreferredName, email, organisation.getId());

        ParameterMap systemParameters = parameterStoreService.getSystemParameters();
        ParameterMap organizationParameters = parameterStoreService.getOrganizationParameters(organisation.getId());

        String slug = orgDisplayName
                .replace(' ', '_')
                .toLowerCase(Locale.ENGLISH);
        organizationParameters.set(Parameters.ORGANISATION_HOSTS, List.of(slug + '.' + systemParameters.stringVal(Parameters.SYSTEM_HOST)));

        Policy adminRole = securityService.save(organisation.getId(), Policy.of("Admin", Statement.allow("*", "*")));
        securityService.assignRole(userDetailRecord, adminRole);
    }
}
