package nz.jive.hub.handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import nz.jive.hub.Parameters;
import nz.jive.hub.SecurityValues;
import nz.jive.hub.Server;
import nz.jive.hub.api.AdminQueryResp;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.database.generated.tables.records.UserDetailRecord;
import nz.jive.hub.service.ParameterStoreService;
import nz.jive.hub.service.SecurityValidationService;
import nz.jive.hub.service.parameters.ParameterMap;
import nz.jive.hub.service.security.Policy;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Goodie
 */
public class AdminQueryHandler implements Handler {
    private final SecurityValidationService securityValidationService;
    private final ParameterStoreService parameterStoreService;

    public AdminQueryHandler(SecurityValidationService securityValidationService, ParameterStoreService parameterStoreService) {
        this.securityValidationService = securityValidationService;
        this.parameterStoreService = parameterStoreService;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        OrganisationRecord organisation = Objects.requireNonNull(ctx.attribute(Server.ATTR_ORGANISATION));
        Policy userPolicy = Objects.requireNonNull(ctx.attribute(Server.ATTR_USER_POLICY));
        UserDetailRecord userDetailRecord = ctx.attribute(Server.ATTR_USER);
        ParameterMap organizationParameters = parameterStoreService.getOrganizationParameters(organisation.getId());

        Map<String, String> collect = Stream.of(Parameters.values())
                .filter(parameter -> StringUtils.startsWith(parameter.getName(), "organisation."))
                .filter(parameter -> securityValidationService.check(userPolicy, SecurityValues.ADMIN_VIEW_VALUE, parameter.getName()))
                .collect(Collectors.toMap(
                        Parameters::getName,
                        organizationParameters::stringVal
                ));

        ctx.json(new AdminQueryResp(collect));
    }
}
