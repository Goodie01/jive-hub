package nz.jive.hub;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.HandlerType;
import nz.jive.hub.database.DatabaseService;
import nz.jive.hub.database.Generator;
import nz.jive.hub.database.Migrator;
import nz.jive.hub.facade.OrganisationFacade;
import nz.jive.hub.handlers.*;
import nz.jive.hub.service.*;
import org.jooq.impl.DSL;

import java.sql.SQLException;
import java.util.Objects;

import static nz.jive.hub.database.generated.Tables.ORGANISATION;

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
        final ObjectMapper objectMapper = new ObjectMapper();
        DatabaseService databaseService = new DatabaseService();
        OrganisationService organisationService = new OrganisationService(databaseService.getConfiguration());
        PageService pageService = new PageService(databaseService.getConfiguration());
        ParameterStoreService parameterStoreService = new ParameterStoreService(databaseService.getConfiguration(), objectMapper);
        UserService userService = new UserService(databaseService.getConfiguration());
        UserSessionService userSessionService = new UserSessionService(databaseService.getConfiguration());
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
                .before(ctx -> Server.setup(ctx, databaseService, securityService))
                .addHttpHandler(HandlerType.GET, "api/health", new HealthCheckHandler(databaseService))
                .addHttpHandler(HandlerType.POST, "api/v1/login", new LoginHandler(userService, userSessionService))
                .addHttpHandler(HandlerType.GET, "api/v1/home", new HomeHandler(securityValidationService, pageService, parameterStoreService))
                .addHttpHandler(HandlerType.GET, "api/v1/pages/*", new PagesHandler(pageService))
                .addHttpHandler(HandlerType.GET, "api/v1/admin", new AdminQueryHandler(securityValidationService, parameterStoreService))
                .start(JiveConfiguration.SERVER_PORT.intVal());
    }
}