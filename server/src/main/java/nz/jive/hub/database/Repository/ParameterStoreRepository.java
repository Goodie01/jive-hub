package nz.jive.hub.database.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import nz.jive.hub.database.Repository.parameters.ParameterMap;
import nz.jive.hub.database.Repository.parameters.ParameterMapImpl;
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
        return getParameters(configuration, null, null, null);
    }

    public ParameterMap getOrganizationParameters(final Configuration configuration, final Integer organizationId) {
        return getParameters(configuration, organizationId, null, null);
    }

    public ParameterMap getUserParameters(final Configuration configuration, final Integer organisationId, final Integer userId) {
        return getParameters(configuration, organisationId, userId, null);
    }

    public ParameterMap getEventParameters(final Configuration configuration, final Integer organisationId, final Integer eventId) {
        return getParameters(configuration, organisationId, null, eventId);
    }

    private ParameterMap getParameters(final Configuration configuration, final Integer organisationId, final Integer userId, Integer eventId) {
        Map<String, String> collect = DSL
                .using(configuration)
                .selectFrom(PARAMETERS)
                .where(PARAMETERS.USER_ID
                        .eq(userId)
                        .or(DSL.val(userId).isNull()))
                .and(PARAMETERS.ORGANISATION_ID
                        .eq(organisationId)
                        .or(DSL.val(organisationId).isNull()))
                .and(PARAMETERS.ORGANISATION_ID
                        .eq(organisationId)
                        .or(DSL.val(organisationId).isNull()))
                .stream()
                .collect(Collectors.toMap(
                        ParametersRecord::getParameterName,
                        ParametersRecord::getValue));

        return new ParameterMapImpl(collect, configuration, objectMapper, organisationId, userId);
    }
}
