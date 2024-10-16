package nz.jive.hub.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.stream.Collectors;
import nz.jive.hub.database.generated.tables.records.ParametersRecord;
import nz.jive.hub.service.parameters.ParameterMap;
import nz.jive.hub.service.parameters.ParameterMapImpl;
import org.jooq.Configuration;
import org.jooq.impl.DSL;

import static nz.jive.hub.database.generated.Tables.PARAMETERS;

/**
 * @author Goodie
 */
public class ParameterStoreService {
    private final Configuration configuration;
    private final ObjectMapper objectMapper;

    public ParameterStoreService(Configuration configuration, ObjectMapper objectMapper) {
        this.configuration = configuration;
        this.objectMapper = objectMapper;
    }

    public ParameterMap getSystemParameters() {
        return getUserParameters(null, null);
    }

    public ParameterMap getOrganizationParameters(final Integer organizationId) {
        return getUserParameters(organizationId, null);
    }

    public ParameterMap getUserParameters(final Integer organisationId, final Integer userId) {
        Map<String, String> collect = DSL
            .using(configuration)
            .selectFrom(PARAMETERS)
            .where(PARAMETERS.USER_ID
                .eq(userId)
                .or(DSL
                    .val(userId)
                    .isNull())
                .and(PARAMETERS.ORGANISATION_ID
                    .eq(organisationId)
                    .or(DSL
                        .val(organisationId)
                        .isNull())))
            .stream()
            .collect(Collectors.toMap(
                ParametersRecord::getParameterName,
                ParametersRecord::getValue));

        return new ParameterMapImpl(collect, configuration, objectMapper, organisationId, userId);
    }
}
