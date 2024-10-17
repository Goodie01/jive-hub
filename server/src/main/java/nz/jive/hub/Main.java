package nz.jive.hub;

import io.javalin.Javalin;
import java.sql.DriverManager;
import java.time.OffsetDateTime;
import java.util.Objects;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import org.flywaydb.core.Flyway;
import org.jooq.Field;
import org.jooq.RecordContext;
import org.jooq.RecordListener;
import org.jooq.SQLDialect;
import org.jooq.codegen.GenerationTool;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultRecordListenerProvider;
import org.jooq.meta.jaxb.Database;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Jdbc;
import org.jooq.meta.jaxb.Target;

/**
 * @author Goodie
 */
public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 1 && Objects.equals(args[0], "server")) {
            migrate();
            run();
        } else if (args.length == 1 && Objects.equals(args[0], "db:generate")) {
            migrate();
            generate();
        } else {
            System.err.println("Please select a valid argument ('server', 'db:generate')");
            System.exit(1);
        }
    }

    private static void generate() throws Exception {
        org.jooq.meta.jaxb.Configuration configuration = new org.jooq.meta.jaxb.Configuration()

                // Configure the database connection here
                .withJdbc(new Jdbc()
                        .withDriver("org.postgresql.Driver")
                        .withUrl(Configuration.DATABASE_JDBC.valueOf())
                )
                .withGenerator(new Generator()
                        .withDatabase(new Database()
                                .withName("org.jooq.meta.postgres.PostgresDatabase")
                                .withIncludes(".*")
                                .withInputSchema("public")
                        )
                        .withTarget(new Target()
                                .withPackageName("nz.jive.hub.database.generated")
                                .withDirectory("\\src\\main\\java")
                        )
                );


        GenerationTool.generate(configuration);
    }

    private static void migrate() {
        var flyway = Flyway.configure()
                .cleanDisabled(false)
                .cleanOnValidationError(true)
                .dataSource(Configuration.DATABASE_JDBC.valueOf(), "", "")
                .load();

        flyway.migrate();
    }

    private static void run() {
        System.out.println("Hello World");
        Javalin.create(javalinConfig -> {
        })
        .get("health", context -> {
            System.setProperty("org.jooq.no-logo", "true");
            System.setProperty("org.jooq.no-tips", "true");

            var connection = DriverManager.getConnection(Configuration.DATABASE_JDBC.valueOf());
            var configuration = new DefaultConfiguration()
                    .set(connection)
                    .set(SQLDialect.POSTGRES)
                    .set(new DefaultRecordListenerProvider(new InsertListener()));

            Integer i = DSL.using(configuration).selectOne().fetchOne().value1();

            if (i == 1) {
                context.result("Hello there");
            } else {
                context.result("Oh no");
            }

            OrganisationRecord organisationRecord = new OrganisationRecord();
            organisationRecord.attach(configuration);
            organisationRecord.setId("test");
            organisationRecord.setDisplayName("Testy mc testerson");
            organisationRecord.store();
        })
        .start(8080);
    }


    private static class InsertListener implements RecordListener {
        private static final Field<OffsetDateTime> CREATED_DATE_FIELD = DSL.field(DSL.name("created_date"), OffsetDateTime.class);
        private static final Field<OffsetDateTime> UPDATED_DATE_FIELD = DSL.field(DSL.name("last_updated_date"), OffsetDateTime.class);

        @Override
        public void insertStart(RecordContext ctx) {
            if (ctx.record().field(CREATED_DATE_FIELD) != null) {
                ctx.record().set(CREATED_DATE_FIELD, OffsetDateTime.now());
            }
        }

        @Override
        public void updateStart(RecordContext ctx) {
            if (ctx.record().field(UPDATED_DATE_FIELD) != null) {
                ctx.record().set(UPDATED_DATE_FIELD, OffsetDateTime.now());
            }
        }
    }
}