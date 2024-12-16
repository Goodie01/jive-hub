package nz.jive.hub.facade;

import nz.jive.hub.Parameters;
import nz.jive.hub.Repository.ParameterStoreRepository;
import nz.jive.hub.Repository.parameters.ParameterMap;
import nz.jive.hub.api.ConfigurationValue;
import nz.jive.hub.database.DatabaseService;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.service.SecurityValidationService;
import nz.jive.hub.service.security.SecurityValues;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Goodie
 */
public class AdminFacade {
    private final DatabaseService databaseService;
    private final ParameterStoreRepository parameterStoreRepository;

    public AdminFacade(final DatabaseService databaseService,
                       final ParameterStoreRepository parameterStoreRepository) {
        this.databaseService = databaseService;
        this.parameterStoreRepository = parameterStoreRepository;
    }

    public @NotNull Set<ConfigurationValue> getValues(
            final OrganisationRecord organisation,
            final SecurityValidationService securityValidationService
    ) {
        ParameterMap organizationParameters = parameterStoreRepository.getOrganizationParameters(databaseService.getConfiguration(), organisation.getId());

        return Stream.of(Parameters.values())
                .filter(parameter -> StringUtils.startsWith(parameter.getName(), "organisation."))
                .filter(parameter -> securityValidationService.check(SecurityValues.ADMIN_VIEW_VALUE, parameter.getName()))
                .map(parameter -> new ConfigurationValue(
                        parameter.getName(),
                        organizationParameters.stringVal(parameter),
                        securityValidationService.check(SecurityValues.ADMIN_WRITE_VALUE, parameter.getName())))
                .collect(Collectors.toSet());
    }
}
