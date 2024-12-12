package nz.jive.hub;

/**
 * @author thomas.goodwin
 */
public enum Parameters {
    SYSTEM_HOST("system.host", "jive-hub.local"),
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
}
