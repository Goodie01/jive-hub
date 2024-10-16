package nz.jive.hub.service.security;

import java.util.List;
import java.util.Objects;

/**
 * @author Goodie
 */
public record Statement(Effect effect, List<String> action, List<String> resource) {
    public static Statement allow(final String action, final String resource) {
        return new Statement(Effect.ALLOW, List.of(action), List.of(resource));
    }

    public static Statement deny(final String action, final String resource) {
        return new Statement(Effect.ALLOW, List.of(action), List.of(resource));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Statement statement = (Statement) o;
        return effect == statement.effect && Objects.equals(action, statement.action) && Objects.equals(resource, statement.resource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(effect, action, resource);
    }
}
