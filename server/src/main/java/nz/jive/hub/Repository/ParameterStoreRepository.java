package nz.jive.hub.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import nz.jive.hub.Repository.parameters.ParameterMap;
import nz.jive.hub.Repository.parameters.ParameterMapImpl;
import nz.jive.hub.database.generated.tables.records.ParametersRecord;
import org.jooq.Configuration;
import org.jooq.impl.DSL;

import java.util.Map;
import java.util.stream.Collectors;

import static nz.jive.hub.database.generated.Tables.PARAMETERS;

/**
 * @author Goodie
 */
public class ParameterStoreRepository {
    private final ObjectMapper objectMapper;

    public ParameterStoreRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ParameterMap getSystemParameters(final Configuration configuration) {
        return getUserParameters(configuration, null, null);
    }

    public ParameterMap getOrganizationParameters(final Configuration configuration, final Integer organizationId) {
        return getUserParameters(configuration, organizationId, null);
    }

    public ParameterMap getUserParameters(final Configuration configuration, final Integer organisationId, final Integer userId) {
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
