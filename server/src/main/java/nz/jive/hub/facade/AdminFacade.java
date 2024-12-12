package nz.jive.hub.facade;

import nz.jive.hub.Parameters;
import nz.jive.hub.Repository.ParameterStoreRepository;
import nz.jive.hub.Repository.parameters.ParameterMap;
import nz.jive.hub.database.DatabaseService;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.service.SecurityValidationService;
import nz.jive.hub.service.security.Policy;
import nz.jive.hub.service.security.SecurityValues;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Goodie
 */
public class AdminFacade {
    private final DatabaseService databaseService;
    private final SecurityValidationService securityValidationService;
    private final ParameterStoreRepository parameterStoreRepository;

    public AdminFacade(final DatabaseService databaseService,
                       final SecurityValidationService securityValidationService,
                       final ParameterStoreRepository parameterStoreRepository) {
        this.databaseService = databaseService;
        this.securityValidationService = securityValidationService;
        this.parameterStoreRepository = parameterStoreRepository;
    }

    public Map<String, String> getValues(
            final OrganisationRecord organisation,
            final Policy userPolicy
    ) {
        ParameterMap organizationParameters = parameterStoreRepository.getOrganizationParameters(databaseService.getConfiguration(), organisation.getId());

        return Stream.of(Parameters.values())
                .filter(parameter -> StringUtils.startsWith(parameter.getName(), "organisation."))
                .filter(parameter -> {
                    return securityValidationService.check(userPolicy, organisation.getSlug() + SecurityValues.ADMIN_VIEW_VALUE, parameter.getName());
                })
                .collect(Collectors.toMap(
                        Parameters::getName,
                        organizationParameters::stringVal
                ));
    }
}
