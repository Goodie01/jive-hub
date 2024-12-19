package nz.jive.hub;

import java.util.Arrays;

/**
 * @author thomas.goodwin
 */
public enum Parameters {
    SYSTEM_HOST("system.host", "jive-hub.local"),
    SYSTEM_TIMEZONE("system.timezone", "Pacific/Auckland"),
    SCHOOL_ENABLED("organisation.school.enabled", "false"),
    EVENT_ENABLED("organisation.event.enabled", "false"),
    ;

    private final String name;
    private final String defaultValue;

    Parameters(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public static Parameters fromString(String name) {
        return Arrays.stream(Parameters.values())
                .filter(parameters -> parameters.getName().equals(name))
                .findFirst()
                .orElseThrow();
    }
}
