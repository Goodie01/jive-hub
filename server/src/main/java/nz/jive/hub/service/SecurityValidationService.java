package nz.jive.hub.service;

import nz.jive.hub.service.security.Effect;
import nz.jive.hub.service.security.Policy;
import nz.jive.hub.service.security.SecurityMatcher;
import nz.jive.hub.service.security.Statement;

import java.util.Objects;

/**
 * @author Goodie
 */
public class SecurityValidationService {
    private final Policy policy;
    private final String nameSpace;

    public SecurityValidationService(Policy policy, String nameSpace) {
        this.policy = Objects.requireNonNull(policy);
        this.nameSpace = Objects.requireNonNull(nameSpace);
    }

    public boolean check(final String action) {
        return check(action, null);
    }

    public void checkThrows(final String action) {
        checkThrows(action, null);
    }

    public void checkThrows(final String action, final String resource) {
        boolean allowed = check(action, resource);

        if (!allowed) {
            throw new IllegalArgumentException("No");
        }
    }


    public boolean check(final String action, final String resource) {
        Objects.requireNonNull(action);

        boolean allowed = false;
        for (Statement statement : policy.statements()) {
            if (checkIfNamespaceMatches(statement) && checkIfActionMatches(action, statement) && checkIfResourceMatches(resource, statement)) {
                if (statement.effect() == Effect.DENY) {
                    return false;
                } else {
                    allowed = true;
                }
            }
        }

        return allowed;
    }

    private static boolean checkIfActionMatches(String action, Statement statement) {
        return statement
                .action()
                .stream()
                .anyMatch(s -> SecurityMatcher.match(s, action));
    }

    private boolean checkIfNamespaceMatches(Statement statement) {
        return SecurityMatcher.match(statement.namespace(), nameSpace);
    }

    private static boolean checkIfResourceMatches(String resource, Statement statement) {
        if (resource != null) {
            return statement
                    .resource()
                    .stream()
                    .anyMatch(s -> SecurityMatcher.match(s, resource));
        } else {
            return true;
        }
    }
}
