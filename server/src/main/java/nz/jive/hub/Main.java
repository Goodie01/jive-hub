package nz.jive.hub;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.HandlerType;
import nz.jive.hub.database.DatabaseService;
import nz.jive.hub.database.Generator;
import nz.jive.hub.database.Migrator;
import nz.jive.hub.database.generated.Tables;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.facade.OrganisationFacade;
import nz.jive.hub.handlers.HealthCheckHandler;
import nz.jive.hub.handlers.HomeHandler;
import nz.jive.hub.handlers.PagesHandler;
import nz.jive.hub.service.*;
import nz.jive.hub.service.security.Policy;
import org.jooq.Configuration;
import org.jooq.impl.DSL;

import java.sql.SQLException;
import java.util.Objects;

import static nz.jive.hub.database.generated.Tables.*;

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
        final ObjectMapper objectMapper = new ObjectMapper();
        DatabaseService databaseService = new DatabaseService();
        OrganisationService organisationService = new OrganisationService(databaseService.getConfiguration());
        PageService pageService = new PageService(databaseService.getConfiguration());
        ParameterStoreService parameterStoreService = new ParameterStoreService(databaseService.getConfiguration(), objectMapper);
        UserService userService = new UserService(databaseService.getConfiguration());
        SecurityService securityService = new SecurityService(databaseService.getConfiguration(), objectMapper);
        SecurityValidationService securityValidationService = new SecurityValidationService();

        OrganisationFacade organisationFacade = new OrganisationFacade(
                organisationService,
                pageService,
                parameterStoreService,
                userService,
                securityService
        );

        DSL
                .using(databaseService.getConfiguration())
                .deleteFrom(ORGANISATION)
                .execute();
        DSL
                .using(databaseService.getConfiguration())
                .deleteFrom(Tables.USER_DETAIL)
                .execute();
        DSL
                .using(databaseService.getConfiguration())
                .deleteFrom(Tables.PARAMETERS)
                .execute();

        organisationFacade.createOrganisation("Test",
                "Thomas Goodwin",
                "Thomas",
                "jive-hub.test@goodwin.geek.nz"
        );

        Javalin
                .create(javalinConfig -> {
                    javalinConfig.useVirtualThreads = true;
                    javalinConfig.showJavalinBanner = false;
                })
                .before(ctx -> {
                    String host = ctx.header("host");
                    Configuration configuration = databaseService.getConfiguration();

                    DSL
                            .using(configuration)
                            .select(PARAMETERS.ORGANISATION_ID)
                            .from(PARAMETERS)
                            .where(PARAMETERS.VALUE.contains(host))
                            .and(PARAMETERS.PARAMETER_NAME.eq(Parameter.ORGANISATION_HOSTS.getName()))
                            .fetchOptionalInto(Integer.class)
                            .ifPresent(id -> {
                                OrganisationRecord test = DSL
                                        .using(configuration)
                                        .selectFrom(ORGANISATION)
                                        .where(ORGANISATION.ID.equal(id))
                                        .fetchOne();
                                ctx.attribute("organisation", test);

                                if (test != null) {
                                    DSL
                                            .using(configuration)
                                            .selectFrom(USER_DETAIL)
                                            .where(USER_DETAIL.ORGANISATION_ID.equal(test.getId()))
                                            .fetchOptional()
                                            .ifPresent(userDetailRecord -> {
                                                ctx.attribute("user", userDetailRecord);

                                                Policy combinedRoleForUser = securityService.getCombinedRoleForUser(userDetailRecord);
                                                ctx.attribute("userPolicy", combinedRoleForUser);
                                            });
                                }
                            });
                })
                .addHttpHandler(HandlerType.GET, "api/health", HealthCheckHandler.create(databaseService))
                .addHttpHandler(HandlerType.GET, "api/v1/home", HomeHandler.create(databaseService, securityValidationService, pageService))
                .addHttpHandler(HandlerType.GET, "api/v1/pages/*", PagesHandler.create(pageService))
                .start(JiveConfiguration.SERVER_PORT.intVal());
    }
}