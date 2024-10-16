package nz.jive.hub.handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import nz.jive.hub.database.DatabaseService;
import org.jetbrains.annotations.NotNull;
import org.jooq.impl.DSL;

/**
 * @author Goodie
 */
public class HealthCheckHandler implements Handler {
    private final DatabaseService databaseService;

    public static HealthCheckHandler create(final DatabaseService databaseService) {
        return new HealthCheckHandler(databaseService);
    }

    private HealthCheckHandler(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
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
