package nz.jive.hub.api;

import org.jetbrains.annotations.NotNull;

/**
 * @author Goodie
 */
public record MenuItem(String name, String link, int order) implements Comparable<MenuItem> {
    @Override
    public int compareTo(@NotNull MenuItem o) {
        return Integer.compare(order, o.order);
    }
}
