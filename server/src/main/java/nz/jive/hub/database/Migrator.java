package nz.jive.hub.database;

import nz.jive.hub.JiveConfiguration;
import org.flywaydb.core.Flyway;

/**
 * @author thomas.goodwin
 */
public final class Migrator {
    private Migrator() {
    }

    public static void migrate() {
        var flyway = Flyway
            .configure()
            .cleanDisabled(false)
            .cleanOnValidationError(true)
            .dataSource(JiveConfiguration.DATABASE_JDBC.valueOf(), "", "")
            .load();

        flyway.migrate();
    }
}
