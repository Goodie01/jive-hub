package nz.jive.hub.service.parameters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import nz.jive.hub.Parameter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Configuration;
import org.jooq.impl.DSL;

import static nz.jive.hub.database.generated.Tables.PARAMETERS;

/**
 * @author Goodie
 */
public class ParameterMapImpl implements ParameterMap {
    private final Map<String, String> parameters;
    private final Configuration configuration;
    private final Integer organisationId;
    private final Integer userId;
    private final ObjectMapper objectMapper;


    public ParameterMapImpl(Map<String, String> parameters, final Configuration configuration, ObjectMapper objectMapper, Integer organisationId, Integer userId) {
        this.parameters = parameters;
        this.configuration = configuration;
        this.objectMapper = objectMapper;
        this.organisationId = organisationId;
        this.userId = userId;
    }

    @Override
    public String stringVal(Parameter tParameter) {
        return parameters.getOrDefault(tParameter.getName(), tParameter.getDefaultValue());
    }

    @Override
    public List<String> stringListVal(Parameter tParameter) {
        String orDefault = parameters.getOrDefault(tParameter.getName(), tParameter.getDefaultValue());
        return readValue(orDefault);
    }

    @Override
    public void set(Parameter tParameter, String value) {
        setInternal(tParameter, value);
    }

    public void set(Parameter tParameter, List<Object> values) {
        String s = getWriteValueAsString(values);
        setInternal(tParameter, s);
    }


    private void setInternal(Parameter tParameter, String value) {
        if (StringUtils.equals(value, tParameter.getDefaultValue()) && parameters.get(tParameter.getName()) != null) {
            DSL
                .using(configuration)
                .deleteFrom(PARAMETERS)
                .where(PARAMETERS.USER_ID
                    .eq(userId)
                    .or(DSL
                        .val(userId)
                        .isNull())
                    .and(PARAMETERS.ORGANISATION_ID
                        .eq(organisationId)
                        .or(DSL
                            .val(organisationId)
                            .isNull()))
                    .and(PARAMETERS.PARAMETER_NAME.eq(tParameter.getName())))
                .execute();
            parameters.remove(tParameter.getName());
        } else if (!StringUtils.equals(value, tParameter.getDefaultValue())) {
            DSL
                .using(configuration)
                .insertInto(PARAMETERS)
                .columns(PARAMETERS.PARAMETER_NAME, PARAMETERS.VALUE, PARAMETERS.ORGANISATION_ID, PARAMETERS.USER_ID, PARAMETERS.CREATED_DATE)
                .values(tParameter.getName(), value, organisationId, userId, OffsetDateTime.now())
                .onDuplicateKeyUpdate()
                .set(PARAMETERS.VALUE, value)
                .set(PARAMETERS.LAST_UPDATED_DATE, OffsetDateTime.now())
                .execute();
            parameters.put(tParameter.getName(), value);
        }
    }

    private String getWriteValueAsString(List<Object> values) {
        try {
            return objectMapper.writeValueAsString(values);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> readValue(String orDefault) {
        try {
            return objectMapper.readValue(orDefault, new TypeReference<List<String>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
