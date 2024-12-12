package nz.jive.hub;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.HandlerType;
import nz.jive.hub.Repository.*;
import nz.jive.hub.database.DatabaseService;
import nz.jive.hub.database.Generator;
import nz.jive.hub.database.Migrator;
import nz.jive.hub.facade.*;
import nz.jive.hub.handlers.*;
import nz.jive.hub.service.SecurityValidationService;
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
        ObjectMapper objectMapper = new ObjectMapper();
        DatabaseService databaseService = new DatabaseService();

        OrganisationRepository organisationRepository = new OrganisationRepository();
        PageRepository pageRepository = new PageRepository();
        ParameterStoreRepository parameterStoreRepository = new ParameterStoreRepository(objectMapper);
        UserRepository userRepository = new UserRepository();
        UserSessionRepository userSessionRepository = new UserSessionRepository();
        SecurityRepository securityRepository = new SecurityRepository(objectMapper);
        HostsRepository hostsRepository = new HostsRepository();

        SecurityValidationService securityValidationService = new SecurityValidationService();

        OrganisationFacade organisationFacade = new OrganisationFacade(
                databaseService,
                organisationRepository,
                pageRepository,
                parameterStoreRepository,
                userRepository,
                securityRepository,
                hostsRepository);
        UserSessionFacade userSessionFacade = new UserSessionFacade(
                databaseService,
                userRepository,
                userSessionRepository);
        AdminFacade adminFacade = new AdminFacade(
                databaseService,
                securityValidationService,
                parameterStoreRepository);
        MenuFacade menuFacade = new MenuFacade(
                databaseService,
                parameterStoreRepository,
                pageRepository,
                securityValidationService);
        SecurityFacade securityFacade = new SecurityFacade(
                databaseService,
                securityRepository,
                objectMapper);
        PageFacade pageFacade = new PageFacade(
                databaseService,
                pageRepository);
        SessionFacade sessionFacade = new SessionFacade();

        DSL.using(databaseService.getConfiguration())
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
                .before(ctx -> Server.setup(ctx, sessionFacade, userSessionFacade, organisationFacade, securityFacade))
                .addHttpHandler(HandlerType.GET, "api/health", new HealthCheckHandler(databaseService))
                .addHttpHandler(HandlerType.POST, "api/v1/login", new LoginHandler(userSessionFacade, sessionFacade))
                .addHttpHandler(HandlerType.GET, "api/v1/home", new HomeHandler(menuFacade, sessionFacade))
                .addHttpHandler(HandlerType.GET, "api/v1/admin", new AdminQueryHandler(adminFacade, sessionFacade))
                .addHttpHandler(HandlerType.GET, "api/v1/pages/*", new PagesHandler(pageFacade, sessionFacade))
                .start(JiveConfiguration.SERVER_PORT.intVal());
    }
}