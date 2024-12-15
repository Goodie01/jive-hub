package nz.jive.hub.service;

import nz.jive.hub.service.security.Effect;
import nz.jive.hub.service.security.Policy;
import nz.jive.hub.service.security.SecurityMatcher;
import nz.jive.hub.service.security.Statement;

/**
 * @author Goodie
 */
public class SecurityValidationService {
    private final Policy policy;
    private final String nameSpace;

    public SecurityValidationService(Policy policy, String nameSpace) {
        this.policy = policy;
        this.nameSpace = nameSpace;
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
