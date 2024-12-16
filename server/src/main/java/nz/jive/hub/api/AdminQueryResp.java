package nz.jive.hub.api;

import java.util.Set;

/**
 * @author Goodie
 */
public record AdminQueryResp(Set<ConfigurationValue> parameters) {
    public record ConfigurationValue(String name, String value, boolean writeable) {

    }
}
