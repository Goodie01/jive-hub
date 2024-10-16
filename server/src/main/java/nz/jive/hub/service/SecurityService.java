package nz.jive.hub.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import nz.jive.hub.database.generated.tables.records.RoleRecord;
import nz.jive.hub.database.generated.tables.records.UserDetailRecord;
import nz.jive.hub.database.generated.tables.records.UserHasRoleRecord;
import nz.jive.hub.service.security.Policy;
import nz.jive.hub.service.security.Statement;
import org.jooq.Configuration;
import org.jooq.impl.DSL;

import static nz.jive.hub.database.generated.Tables.ROLE;
import static nz.jive.hub.database.generated.Tables.USER_HAS_ROLE;

/**
 * @author Goodie
 */
public class SecurityService {
    private static final Policy DENY_ALL = Policy.of("Default", Statement.deny("*", "*;"));
    private final Configuration configuration;
    private final ObjectMapper objectMapper;

    public SecurityService(Configuration configuration, ObjectMapper objectMapper) {
        this.configuration = configuration;
        this.objectMapper = objectMapper;
    }

    public Policy save(final int organisationId, final Policy policy) {
        RoleRecord roleRecord = new RoleRecord();
        roleRecord.attach(configuration);
        roleRecord.setOrganisationId(organisationId);
        roleRecord.setName(policy.name());
        roleRecord.setPolicy(getPolicyAsString(policy.statements()));
        roleRecord.store();

        return policy;
    }

    private String getPolicyAsString(Set<Statement> statements) {
        try {
            return objectMapper.writeValueAsString(statements);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<Statement> getPolicyStatements(final String stringValue) {
        try {
            return objectMapper.readValue(stringValue, new TypeReference<Set<Statement>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void assignRole(UserDetailRecord userDetailRecord, Policy role) {
        UserHasRoleRecord userHasRoleRecord = new UserHasRoleRecord();
        userHasRoleRecord.attach(configuration);
        userHasRoleRecord.setOrganisationId(userDetailRecord.getOrganisationId());
        userHasRoleRecord.setRoleName(role.name());
        userHasRoleRecord.setUserId(userDetailRecord.getId());
        userHasRoleRecord.store();
    }

    public Policy getCombinedRoleForUser(UserDetailRecord userDetailRecord) {
        return DSL
            .using(configuration)
            .selectFrom(ROLE)
            .where(ROLE.NAME.in(
                DSL
                    .select(USER_HAS_ROLE.ROLE_NAME)
                    .from(USER_HAS_ROLE)
                    .where(USER_HAS_ROLE.USER_ID.eq(userDetailRecord.getId()))
                    .and(USER_HAS_ROLE.ORGANISATION_ID.eq(userDetailRecord.getOrganisationId()))
            ))
            .stream()
            .map(roleRecord -> new Policy(roleRecord.getName(), getPolicyStatements(roleRecord.getPolicy())))
            .reduce(Policy::join)
            .orElse(DENY_ALL);
    }
}
