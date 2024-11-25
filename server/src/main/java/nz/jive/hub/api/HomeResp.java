package nz.jive.hub.api;

import nz.jive.hub.api.domain.User;

import java.util.SortedSet;

/**
 * @author Goodie
 */
public record HomeResp(String displayName, SortedSet<MenuItem> menuItems, User loggedInUser) {
}
