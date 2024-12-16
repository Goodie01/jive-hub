package nz.jive.hub.api;

import java.util.Set;

/**
 * @author thomas.goodwin
 */
public record AdminUpdateReq(Set<UpdateValue> values) {
    public record UpdateValue(String name, String value) {
    }
}
