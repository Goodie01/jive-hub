package nz.jive.hub.service;

import io.javalin.Javalin;
import io.javalin.http.HandlerType;
import nz.jive.hub.facade.SessionFacade;
import nz.jive.hub.handlers.InternalHandler;
import nz.jive.hub.service.security.Policy;
import nz.jive.hub.service.security.SecurityUtils;

import java.util.function.Supplier;

/**
 * @author Goodie
 */
public class ServerService {

    private Javalin javalin;

    public ServerService setUp(Supplier<Javalin> supplier) {
        javalin = supplier.get();

        return this;
    }

    public ServerService get(String path, InternalHandler handler) {
        javalin.addHttpHandler(HandlerType.GET, path, ctx -> {
            SessionFacade sessionFacade = new SessionFacade(ctx);
            Policy policy = sessionFacade.policy().orElseThrow();
            String nameSpace = SecurityUtils.namespace(sessionFacade.organisation().orElseThrow());
            SecurityValidationService securityValidationService = new SecurityValidationService(policy, nameSpace);
            handler.handle(ctx, sessionFacade, securityValidationService);
        });

        return this;
    }

    public ServerService post(String path, InternalHandler handler) {
        javalin.addHttpHandler(HandlerType.POST, path, ctx -> {
            SessionFacade sessionFacade = new SessionFacade(ctx);
            String nameSpace = SecurityUtils.namespace(sessionFacade.organisation().orElseThrow());
            SecurityValidationService securityValidationService = new SecurityValidationService(sessionFacade.policy().orElseThrow(), nameSpace);
            handler.handle(ctx, sessionFacade, securityValidationService);
        });

        return this;
    }

    public void start(int i) {
        javalin.start(i);
    }
}
