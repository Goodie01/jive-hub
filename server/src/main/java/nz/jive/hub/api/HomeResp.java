package nz.jive.hub.api;

import java.util.SortedSet;

/**
 * @author Goodie
 */
public record HomeResp(String displayName, SortedSet<MenuItem> menuItems) {
}
