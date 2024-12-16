package nz.jive.hub.facade;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import nz.jive.hub.Parameters;
import nz.jive.hub.Repository.ParameterStoreRepository;
import nz.jive.hub.Repository.parameters.ParameterMap;
import nz.jive.hub.api.AdminQueryResp;
import nz.jive.hub.api.AdminUpdateReq;
import nz.jive.hub.database.DatabaseService;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.service.SecurityValidationService;
import nz.jive.hub.service.security.SecurityValues;
import nz.jive.hub.utils.Duo;

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

    public Set<AdminQueryResp.ConfigurationValue> getAllValues(final OrganisationRecord organisation,
            final SecurityValidationService securityValidationService) {
        ParameterMap organizationParameters = parameterStoreRepository.getOrganizationParameters(databaseService.getConfiguration(),
                organisation.getId());

        return Stream.of(Parameters.values())
                .filter(parameter -> StringUtils.startsWith(parameter.getName(), "organisation."))
                .filter(parameter -> securityValidationService.check(SecurityValues.ADMIN_VIEW_VALUE, parameter.getName()))
                .map(parameter ->
                        new AdminQueryResp.ConfigurationValue(parameter.getName(), organizationParameters.stringVal(parameter),
                        securityValidationService.check(SecurityValues.ADMIN_WRITE_VALUE, parameter.getName())))
                .collect(Collectors.toSet());
    }

    public void updateValues(OrganisationRecord organisation, SecurityValidationService securityValidationService, AdminUpdateReq adminUpdateReq) {
        ParameterMap organizationParameters = parameterStoreRepository.getOrganizationParameters(databaseService.getConfiguration(), organisation.getId());

        adminUpdateReq.values().stream()
                .map(updateValue -> {
                    Parameters parameter = Parameters.valueOf(updateValue.name());
                    securityValidationService.checkThrows(SecurityValues.ADMIN_WRITE_VALUE, parameter.getName());
                    return new Duo<>(parameter, updateValue.value());
                })
                .forEach(updateValue -> organizationParameters.set(updateValue.one(), updateValue.two()));
    }
}
