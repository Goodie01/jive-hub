package nz.jive.hub.handlers;

import java.util.Set;

import nz.jive.hub.api.AdminQueryResp;
import nz.jive.hub.facade.AdminFacade;
import nz.jive.hub.service.server.ServerContext;

/**
 * @author Goodie
 */
public class AdminQueryHandler implements InternalHandler {
    private final AdminFacade adminFacade;

    public AdminQueryHandler(AdminFacade adminFacade) {
        this.adminFacade = adminFacade;
    }

    @Override
    public void handle(ServerContext serverContext) throws Exception {
        Set<AdminQueryResp.ConfigurationValue> adminParameterValues = adminFacade.getAllValues(serverContext);

        serverContext.getCtx().json(new AdminQueryResp(adminParameterValues));
    }
}
