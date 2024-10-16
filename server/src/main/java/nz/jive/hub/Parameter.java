package nz.jive.hub;

/**
 * @author thomas.goodwin
 */
public class Parameter {
    public static Parameter SYSTEM_HOST = new Parameter("system.host", "jive-hub.local");
    public static Parameter ORGANISATION_HOSTS = new Parameter("organisation.hosts", "[]");

    private final String name;
    private final String defaultValue;

    Parameter(String name, String defaultValue) {
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
