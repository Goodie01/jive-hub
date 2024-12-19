package nz.jive.hub;

import org.apache.commons.lang3.StringUtils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Goodie
 */

public enum JiveConfiguration {
    DATABASE_JDBC,
    SERVER_PORT,
    JAVALIN_DEBUG_LOGGING,
    ;

    public String valueOf() {
        String propertyName = this.toString();
        String propertyFromSystem = System.getenv(propertyName);

        if (StringUtils.isNotBlank(propertyFromSystem)) {
            return propertyFromSystem;
        }

        Properties properties = new Properties();
        String propertyFile = System.getProperty("props", "default.properties");

        try (FileReader propertiesFileReader = new FileReader(propertyFile)) {
            properties.load(propertiesFileReader);
        } catch (IOException | NullPointerException e) {
            throw new IllegalStateException("Unable to load properties file", e);
        }
        return (String) properties.get(propertyName);
    }

    public boolean booleanVal() {
        return Boolean.parseBoolean(valueOf());
    }

    public int intVal() {
        return Integer.parseInt(valueOf());
    }
}
