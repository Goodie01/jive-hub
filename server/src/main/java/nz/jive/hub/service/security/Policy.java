package nz.jive.hub.service.security;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Goodie
 */
public record Policy(String name, Set<Statement> statements) {
    public static Policy of(String name, Statement... statements) {
        List<Statement> list = Arrays.asList(statements);
        return new Policy(name, new HashSet<>(list));
    }

    public static Policy of(String name, Set<Statement> statements) {
        return new Policy(name, statements);
    }

    public Policy join(Policy policy) {
        HashSet<Statement> statements = new HashSet<>();
        statements.addAll(this.statements);
        statements.addAll(policy.statements);

        return Policy.of("Combined", statements);
    }
}
