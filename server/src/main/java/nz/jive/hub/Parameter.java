package nz.jive.hub;

/**
 * @author thomas.goodwin
 */
public class Parameter<T> {
    public static Parameter<Boolean> SCHOOL_ENABLED = new Parameter<>("school.enabled", Boolean.class, true);
    public static Parameter<Boolean> EVENT_ENABLED = new Parameter<>("event.enabled", Boolean.class, true);

    private final String name;
    private final Class<T> tClass;
    private final T defaultValue;

    Parameter(String name, Class<T> tClass, T defaultValue) {
        this.name = name;
        this.tClass = tClass;
        this.defaultValue = defaultValue;
    }
}
