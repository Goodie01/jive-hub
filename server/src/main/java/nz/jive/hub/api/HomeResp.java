package nz.jive.hub.api;

/**
 * @author Goodie
 */
public record HomeResp(String displayName, java.util.SortedSet<MenuItem> menuItems) {
}
