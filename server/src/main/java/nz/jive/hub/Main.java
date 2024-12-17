package nz.jive.hub;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import nz.jive.hub.Repository.*;
import nz.jive.hub.database.DatabaseService;
import nz.jive.hub.database.Generator;
import nz.jive.hub.database.Migrator;
import nz.jive.hub.facade.*;
import nz.jive.hub.handlers.*;
import nz.jive.hub.service.server.ServerService;

import java.sql.SQLException;
import java.util.Objects;

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
                parameterStoreRepository);
        MenuFacade menuFacade = new MenuFacade(
                databaseService,
                parameterStoreRepository,
                pageRepository
        );
        SecurityFacade securityFacade = new SecurityFacade(
                databaseService,
                securityRepository,
                objectMapper);
        PageFacade pageFacade = new PageFacade(
                databaseService,
                pageRepository);
//        DSL.using(databaseService.getConfiguration())
//                .deleteFrom(ORGANISATION)
//                .execute();
//
//        organisationFacade.createOrganisation("This is a test",
//                "Thomas Goodwin",
//                "Thomas",
//                "jive-hub.test@goodwin.geek.nz"
//        );


        new ServerService().setUp(() -> Javalin.create(javalinConfig -> {
                            javalinConfig.useVirtualThreads = true;
                            javalinConfig.showJavalinBanner = false;
                        })
                        .before(ctx -> Server.setup(ctx, userSessionFacade, organisationFacade, securityFacade)))
                .get("api/health", new HealthCheckHandler(databaseService))
                .post("api/v1/login", new LoginHandler(userSessionFacade))
                .get("api/v1/home", new HomeHandler(menuFacade))
                .get("api/v1/admin", new AdminQueryHandler(adminFacade))
                .post("api/v1/admin", new AdminUpdateHandler(adminFacade))
                .get("api/v1/pages/*", new PagesHandler(pageFacade))
                .start(JiveConfiguration.SERVER_PORT.intVal());
    }
}