package nz.jive.hub.facade;

import nz.jive.hub.Parameters;
import nz.jive.hub.SecurityValues;
import nz.jive.hub.api.AdminQueryResp;
import nz.jive.hub.api.AdminUpdateReq;
import nz.jive.hub.database.DatabaseService;
import nz.jive.hub.database.Repository.ParameterStoreRepository;
import nz.jive.hub.database.Repository.parameters.ParameterMap;
import nz.jive.hub.service.server.ServerContext;
import nz.jive.hub.utils.Duo;
import org.apache.commons.lang3.StringUtils;

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

    public Set<AdminQueryResp.ConfigurationValue> getAllValues(final ServerContext serverContext) {
        ParameterMap organizationParameters = parameterStoreRepository.getOrganizationParameters(databaseService.getConfiguration(),
                serverContext.organisation().getId());

        return Stream.of(Parameters.values())
                .filter(parameter -> StringUtils.startsWith(parameter.getName(), "organisation."))
                .filter(parameter -> serverContext.check(SecurityValues.ADMIN_VIEW_VALUE, parameter.getName()))
                .map(parameter ->
                        new AdminQueryResp.ConfigurationValue(parameter.getName(), organizationParameters.stringVal(parameter),
                                serverContext.check(SecurityValues.ADMIN_WRITE_VALUE, parameter.getName())))
                .collect(Collectors.toSet());
    }

    public void updateValues(ServerContext serverContext, AdminUpdateReq adminUpdateReq) {
        ParameterMap organizationParameters = parameterStoreRepository.getOrganizationParameters(databaseService.getConfiguration(), serverContext.organisation().getId());

        adminUpdateReq.values().stream()
                .map(updateValue -> {
                    Parameters parameter = Parameters.fromString(updateValue.name());
                    serverContext.checkThrows(SecurityValues.ADMIN_WRITE_VALUE, parameter.getName());
                    return new Duo<>(parameter, updateValue.value());
                })
                .forEach(updateValue -> organizationParameters.set(updateValue.one(), updateValue.two()));
    }
}
