package nz.jive.hub;

/**
 * @author thomas.goodwin
 */
public class Parameters {
    public static Parameters SYSTEM_HOST = new Parameters("system.host", "jive-hub.local");
    public static Parameters ORGANISATION_HOSTS = new Parameters("organisation.hosts", "[]");
    public static Parameters SCHOOL_ENABLED = new Parameters("organisation.school.enabled", "false");
    public static Parameters EVENT_ENABLED = new Parameters("organisation.event.enabled", "false");

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
