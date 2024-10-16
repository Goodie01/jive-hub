package nz.jive.hub.database;

import nz.jive.hub.JiveConfiguration;
import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Database;
import org.jooq.meta.jaxb.Jdbc;
import org.jooq.meta.jaxb.Target;

/**
 * @author thomas.goodwin
 */
public final class Generator {
    private Generator() {
    }

    public static void generate() throws Exception {
        Configuration configuration = new Configuration()
            .withJdbc(
                new Jdbc()
                    .withDriver("org.postgresql.Driver")
                    .withUrl(JiveConfiguration.DATABASE_JDBC.valueOf()))
            .withGenerator(
                new org.jooq.meta.jaxb.Generator()
                    .withDatabase(
                        new Database()
                            .withName("org.jooq.meta.postgres.PostgresDatabase")
                            .withIncludes(".*")
                            .withInputSchema("public"))
                    .withTarget(
                        new Target()
                            .withPackageName("nz.jive.hub.database.generated")
                            .withDirectory("\\src\\main\\java")
                    )
            );


        GenerationTool.generate(configuration);
    }
}
