package nz.jive.hub.facade;

import nz.jive.hub.Parameters;
import nz.jive.hub.Repository.*;
import nz.jive.hub.Repository.parameters.ParameterMap;
import nz.jive.hub.database.DatabaseService;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.database.generated.tables.records.UserDetailRecord;
import nz.jive.hub.service.security.Policy;
import nz.jive.hub.service.security.SecurityUtils;
import nz.jive.hub.service.security.Statement;
import org.jooq.impl.DSL;

import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author thomas.goodwin
 */
public class OrganisationFacade {
    private static final Pattern DISPLAY_NAME_VALIDATOR = Pattern.compile("^([a-zA-Z0-9\\s-])+$");

    private final DatabaseService databaseService;
    private final OrganisationRepository organisationRepository;
    private final PageRepository pageRepository;
    private final ParameterStoreRepository parameterStoreRepository;
    private final UserRepository userRepository;
    private final SecurityRepository securityRepository;
    private final HostsRepository hostsRepository;

    public OrganisationFacade(final DatabaseService databaseService,
                              final OrganisationRepository organisationRepository,
                              final PageRepository pageRepository,
                              final ParameterStoreRepository parameterStoreRepository,
                              final UserRepository userRepository,
                              final SecurityRepository securityRepository,
                              final HostsRepository hostsRepository) {
        this.databaseService = databaseService;
        this.organisationRepository = organisationRepository;
        this.pageRepository = pageRepository;
        this.parameterStoreRepository = parameterStoreRepository;
        this.userRepository = userRepository;
        this.securityRepository = securityRepository;
        this.hostsRepository = hostsRepository;
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

        DSL.using(databaseService.getConfiguration()).transaction(configuration -> {
            OrganisationRecord organisation = organisationRepository.createOrganisation(configuration, orgDisplayName);

            pageRepository.save(
                    configuration, organisation.getId(), "",
                    """
                            # #orgName
                            This is your Jive Hub home page!
                            """.replace("#orgName", orgDisplayName),
                    orgDisplayName + " - Home",
                    "Home", 0);

            UserDetailRecord userDetailRecord = userRepository.create(configuration, userName, userPreferredName, email, organisation.getId());

            ParameterMap systemParameters = parameterStoreRepository.getSystemParameters(configuration);
            String slug = orgDisplayName.replaceAll("\\s", "_").toLowerCase(Locale.ENGLISH); //TODO make unique
            String hostName = slug + '.' + systemParameters.stringVal(Parameters.SYSTEM_HOST);
            hostsRepository.addHostName(configuration, organisation, hostName);

            String nameSpace = SecurityUtils.namespace(organisation);
            Policy adminRole = securityRepository.save(configuration, organisation.getId(), Policy.of("Admin", Statement.allow(nameSpace, "*", ".*")));
            securityRepository.assignRole(configuration, userDetailRecord, adminRole);
        });
    }

    public Optional<OrganisationRecord> findFromHostName(final String hostName) {
        return organisationRepository.findFromHostName(databaseService.getConfiguration(), hostName);
    }
}
