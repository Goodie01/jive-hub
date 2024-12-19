package nz.jive.hub;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import nz.jive.hub.database.DatabaseService;
import nz.jive.hub.database.Generator;
import nz.jive.hub.database.Migrator;
import nz.jive.hub.database.Repository.*;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.facade.*;
import nz.jive.hub.handlers.*;
import nz.jive.hub.perf.TimingResults;
import nz.jive.hub.service.server.ServerService;
import org.jooq.impl.DSL;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
        EventsRepository eventsRepository = new EventsRepository();

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
        EventsFacade eventsFacade = new EventsFacade(
                databaseService,
                parameterStoreRepository,
                eventsRepository
        );
        SecurityFacade securityFacade = new SecurityFacade(
                databaseService,
                securityRepository,
                objectMapper);
        PageFacade pageFacade = new PageFacade(
                databaseService,
                pageRepository);

        Integer i = DSL.using(databaseService.getConfiguration())
                .selectCount()
                .from(ORGANISATION)
                .fetchOne(0, int.class);

        if (i == 0) {
            OrganisationRecord organisationRecord = organisationFacade.createOrganisation("This is a test",
                    "Thomas Goodwin",
                    "Thomas",
                    "jive-hub.test@goodwin.geek.nz"
            );
            eventsFacade.createEvent(organisationRecord, "My test event", "Only the best",
                    OffsetDateTime.of(LocalDateTime.of(2026, 3, 6, 17, 0, 0), ZoneOffset.ofHours(12)),
                    OffsetDateTime.of(LocalDateTime.of(2026, 3, 8, 17, 0, 0), ZoneOffset.ofHours(12))
            );
        }

        TimingResults.getInstance().dumpCurrent();

        new ServerService().setUp(() -> Javalin.create(javalinConfig -> {
                                    javalinConfig.useVirtualThreads = true;
                                    javalinConfig.showJavalinBanner = false;

                                    if (JiveConfiguration.JAVALIN_DEBUG_LOGGING.booleanVal()) {
                                        javalinConfig.bundledPlugins.enableDevLogging();
                                    }
                                })
                                .before(ctx -> Server.setup(ctx, userSessionFacade, organisationFacade, securityFacade))
                                .after(_ -> TimingResults.getInstance().dumpCurrent())
                )
                .get("api/health", new HealthCheckHandler(databaseService))
                .post("api/v1/login", new LoginHandler(userSessionFacade))
                .get("api/v1/home", new HomeHandler(menuFacade, eventsFacade))
                .get("api/v1/admin", new AdminQueryHandler(adminFacade))
                .post("api/v1/admin", new AdminUpdateHandler(adminFacade))
                .get("api/v1/pages/*", new PagesHandler(pageFacade))
                .start(JiveConfiguration.SERVER_PORT.intVal());
    }
}