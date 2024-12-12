package nz.jive.hub.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.jive.hub.Repository.SecurityRepository;
import nz.jive.hub.database.DatabaseService;
import nz.jive.hub.database.generated.tables.records.UserDetailRecord;
import nz.jive.hub.service.security.Policy;
import nz.jive.hub.service.security.Statement;

import java.util.Set;

/**
 * @author Goodie
 */
public class SecurityFacade {
    private static final Policy DENY_ALL = Policy.of("Default", Statement.deny("*", "*"));
    private final DatabaseService databaseService;
    private final SecurityRepository securityRepository;
    private final ObjectMapper objectMapper;

    public SecurityFacade(DatabaseService databaseService, SecurityRepository securityRepository, ObjectMapper objectMapper) {
        this.databaseService = databaseService;
        this.securityRepository = securityRepository;
        this.objectMapper = objectMapper;
    }

    public Policy getCombinedRoleForUser(UserDetailRecord userDetailRecord) {
        return securityRepository.getAllPolicies(databaseService.getConfiguration(), userDetailRecord)
                .stream()
                .map(roleRecord -> new Policy(roleRecord.getName(), getPolicyStatements(roleRecord.getPolicy())))
                .reduce(Policy::join)
                .orElse(DENY_ALL);
    }

    private Set<Statement> getPolicyStatements(final String stringValue) {
        try {
            return objectMapper.readValue(stringValue, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Policy getDenyAllPolicy() {
        return DENY_ALL;
    }
}
