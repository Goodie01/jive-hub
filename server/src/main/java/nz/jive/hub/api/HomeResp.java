package nz.jive.hub.api;

import java.util.List;

/**
 * @author Goodie
 */
public record HomeResp(String displayName, List<MenuItem> menuItems) {
}
