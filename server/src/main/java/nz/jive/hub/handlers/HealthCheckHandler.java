package nz.jive.hub.handlers;

import org.jooq.impl.DSL;

import nz.jive.hub.database.DatabaseService;
import nz.jive.hub.service.server.ServerContext;

/**
 * @author Goodie
 */
public class HealthCheckHandler implements InternalHandler {
    private final DatabaseService databaseService;

    public HealthCheckHandler(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Override
    public void handle(ServerContext serverContext) throws Exception {
        Integer i = DSL.using(databaseService.getConfiguration()).selectOne().fetchOne().value1();

        if (i == 1) {
            serverContext.getCtx().result("OK");
            serverContext.getCtx().status(200);
        } else {
            serverContext.getCtx().result("Oh no");
            serverContext.getCtx().status(500);
        }
    }
}
