package nz.jive.hub.service.server;

import io.javalin.Javalin;
import io.javalin.http.HandlerType;
import nz.jive.hub.facade.SessionFacade;
import nz.jive.hub.handlers.InternalHandler;
import nz.jive.hub.service.SecurityValidationService;
import nz.jive.hub.service.security.Policy;
import nz.jive.hub.service.security.SecurityUtils;

import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

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
        return setUpHandler(HandlerType.GET, path, handler);
    }

    public ServerService post(String path, InternalHandler handler) {
        return setUpHandler(HandlerType.POST, path, handler);
    }

    private ServerService setUpHandler(HandlerType handlerType, String path, InternalHandler handler) {
        javalin.addHttpHandler(handlerType, path, ctx -> {
            SessionFacade sessionFacade = new SessionFacade(ctx);
            Policy policy = sessionFacade.policy().orElseThrow();
            String nameSpace = SecurityUtils.namespace(sessionFacade.organisation().orElseThrow());
            SecurityValidationService securityValidationService = new SecurityValidationService(policy, nameSpace);
            handler.handle(new ServerContext(ctx, sessionFacade, securityValidationService));
        });

        return this;
    }

    public void start(int i) {
        javalin.start(i);
    }
}
