package nz.jive.hub.handlers;

import io.javalin.http.Context;
import nz.jive.hub.database.DatabaseService;
import nz.jive.hub.facade.SessionFacade;
import nz.jive.hub.service.SecurityValidationService;
import org.jetbrains.annotations.NotNull;
import org.jooq.impl.DSL;

/**
 * @author Goodie
 */
public class HealthCheckHandler implements InternalHandler {
    private final DatabaseService databaseService;

    public HealthCheckHandler(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Override
    public void handle(@NotNull Context ctx, SessionFacade sessionFacade, SecurityValidationService securityValidationService) throws Exception {
        Integer i = DSL
                .using(databaseService.getConfiguration())
                .selectOne()
                .fetchOne()
                .value1();

        if (i == 1) {
            ctx.result("OK");
            ctx.status(200);
        } else {
            ctx.result("Oh no");
            ctx.status(500);
        }
    }
}
