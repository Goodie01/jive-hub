package nz.jive.hub.handlers;

import io.javalin.http.Context;
import nz.jive.hub.facade.SessionFacade;
import nz.jive.hub.service.SecurityValidationService;
import nz.jive.hub.service.server.ServerContext;

import org.jetbrains.annotations.NotNull;

/**
 * @author Goodie
 */
public interface InternalHandler {
    void handle(@NotNull ServerContext serverContext) throws Exception;
}
