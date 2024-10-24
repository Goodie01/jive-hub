package nz.jive.hub;

import io.javalin.Javalin;
import io.javalin.http.HandlerType;
import io.javalin.router.Endpoint;
import java.sql.SQLException;
import java.util.Objects;
import nz.jive.hub.database.DatabaseService;
import nz.jive.hub.database.Generator;
import nz.jive.hub.database.Migrator;
import nz.jive.hub.database.generated.Tables;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.database.generated.tables.records.ParametersRecord;
import nz.jive.hub.database.generated.tables.records.UserDetailRecord;
import org.jooq.impl.DSL;

/**
 * @author Goodie
 */
public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 1 && Objects.equals(args[0], "server")) {
            Migrator.migrate();
            run();
        } else if (args.length == 1 && Objects.equals(args[0], "db:generate")) {
            Migrator.migrate();
            Generator.generate();
        } else {
            System.err.println("Please select a valid argument ('server', 'db:generate')");
            System.exit(1);
        }
    }

    private static void run() throws SQLException {
        System.out.println("Hello World");
        DatabaseService databaseService = new DatabaseService();
        DSL
            .using(databaseService.getConfiguration())
            .deleteFrom(Tables.ORGANISATION)
            .execute();
        DSL
            .using(databaseService.getConfiguration())
            .deleteFrom(Tables.USER_DETAIL)
            .execute();
        DSL
            .using(databaseService.getConfiguration())
            .deleteFrom(Tables.PARAMETERS)
            .execute();

        OrganisationRecord organisationRecord = new OrganisationRecord();
        organisationRecord.attach(databaseService.getConfiguration());
        organisationRecord.setId("test");
        organisationRecord.setDisplayName("Testy mc testerson");
        organisationRecord.store();

        UserDetailRecord userDetailRecord = new UserDetailRecord();
        userDetailRecord.attach(databaseService.getConfiguration());
        userDetailRecord.setDisplayName("Test");
        userDetailRecord.setOrganisationId(organisationRecord.getId());
        userDetailRecord.store();

        ParametersRecord parametersRecord = new ParametersRecord();
        parametersRecord.attach(databaseService.getConfiguration());
        parametersRecord.setOrganisationId(organisationRecord.getId());
        parametersRecord.setParameterName("value");
        parametersRecord.setValue("1");
        parametersRecord.store();

        parametersRecord.setParameterName("Hello there");
        parametersRecord.setValue("This is a value");
        parametersRecord.store();

        parametersRecord.setParameterName("General Kenobi");
        parametersRecord.store();

        Javalin
            .create(javalinConfig -> {
                javalinConfig.useVirtualThreads = true;
                javalinConfig.showJavalinBanner = false;
            })
            .addEndpoint(
                new Endpoint.Companion.EndpointBuilder(HandlerType.GET, "aoeaoe")
                    .handler(ctx -> ctx.result("Hello World"))
            )
            .get("health", context -> {
                Integer i = DSL
                    .using(databaseService.getConfiguration())
                    .selectOne()
                    .fetchOne()
                    .value1();

                if (i == 1) {
                    context.result("Hello there");
                } else {
                    context.result("Oh no");
                }
            })
            .start(8080);
    }
}