package nz.jive.hub.Repository.parameters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.jive.hub.Parameters;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Configuration;
import org.jooq.impl.DSL;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public String stringVal(Parameters tParameter) {
        return parameters.getOrDefault(tParameter.getName(), tParameter.getDefaultValue());
    }

    @Override
    public boolean boolVal(Parameters tParameter) {
        String orDefault = parameters.getOrDefault(tParameter.getName(), tParameter.getDefaultValue());
        return Boolean.parseBoolean(orDefault);
    }

    @Override
    public List<String> stringListVal(Parameters tParameter) {
        String orDefault = parameters.getOrDefault(tParameter.getName(), tParameter.getDefaultValue());
        return readListValue(orDefault);
    }

    @Override
    public void set(Parameters tParameter, String value) {
        setInternal(tParameter, value);
    }

    @Override
    public Map<Parameters, String> getAll() {
        return Stream.of(Parameters.values())
                .filter(parameters -> StringUtils.startsWith(parameters.getName(), "organisation."))
                .collect(Collectors.toMap(
                        Function.identity(),
                        parameter -> parameters.getOrDefault(parameter.getName(), parameter.getDefaultValue())
                ));
    }

    public void set(Parameters tParameter, List<Object> values) {
        String s = writeValueAsString(values);
        setInternal(tParameter, s);
    }


    private void setInternal(Parameters tParameter, String value) {
        if (StringUtils.equals(value, tParameter.getDefaultValue()) && parameters.get(tParameter.getName()) != null) {
            DSL.using(configuration)
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
            DSL.using(configuration)
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

    private String writeValueAsString(List<Object> values) {
        try {
            return objectMapper.writeValueAsString(values);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> readListValue(String orDefault) {
        try {
            return objectMapper.readValue(orDefault, new TypeReference<List<String>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
