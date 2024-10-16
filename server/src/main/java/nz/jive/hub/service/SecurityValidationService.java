package nz.jive.hub.service;

import nz.jive.hub.service.security.Effect;
import nz.jive.hub.service.security.Policy;
import nz.jive.hub.service.security.SecurityMatcher;
import nz.jive.hub.service.security.Statement;

/**
 * @author Goodie
 */
public class SecurityValidationService {
    public boolean check(final Policy policy, final String action) {
        return check(policy, action, null);
    }

    public void checkThrows(final Policy policy, final String action) {
        checkThrows(policy, action, null);
    }

    public boolean check(final Policy policy, final String action, final String resource) {
        boolean allowed = false;
        for (Statement statement : policy.statements()) {
            boolean actionMatches = statement
                .action()
                .stream()
                .anyMatch(s -> SecurityMatcher.match(s, action));
            boolean resourceMatches = checkIfResourceMatches(resource, statement);

            if (actionMatches && resourceMatches) {
                if (statement.effect() == Effect.DENY) {
                    return false;
                } else {
                    allowed = true;
                }
            }
        }

        return allowed;
    }

    public void checkThrows(final Policy policy, final String action, final String resource) {
        boolean allowed = false;
        for (Statement statement : policy.statements()) {
            boolean actionMatches = statement
                .action()
                .stream()
                .anyMatch(s -> SecurityMatcher.match(s, action));
            boolean resourceMatches = checkIfResourceMatches(resource, statement);

            if (actionMatches && resourceMatches) {
                if (statement.effect() == Effect.DENY) {
                    throw new IllegalArgumentException("No");
                } else {
                    allowed = true;
                }
            }
        }

        if (!allowed) {
            throw new IllegalArgumentException("No");
        }
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
