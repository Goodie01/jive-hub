package nz.jive.hub.handlers;

import io.javalin.http.Context;
import nz.jive.hub.Server;
import nz.jive.hub.api.PageResp;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.facade.PageFacade;
import nz.jive.hub.facade.SessionFacade;
import nz.jive.hub.service.SecurityValidationService;
import nz.jive.hub.service.server.ServerContext;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * @author Goodie
 */
public class PagesHandler implements InternalHandler {
    private final PageFacade pageFacade;

    public PagesHandler(PageFacade pageFacade) {
        this.pageFacade = pageFacade;
    }

    @Override
    public void handle(ServerContext serverContext) throws Exception {
        //Object o = ctx.bodyAsClass(Object.class);
        OrganisationRecord organisation = serverContext.organisation();
        String pageId = extractPageId(serverContext.getCtx());

        pageFacade.find(organisation.getId(), pageId)
                .ifPresentOrElse(pageRecord -> {
                    serverContext.getCtx().json(new PageResp(pageRecord.getContent(), pageRecord.getTitle()));
                }, () -> {
                    serverContext.getCtx().json(new PageResp("Page Missing", "404 Not Found"));
                });
    }

    @NotNull
    private static String extractPageId(@NotNull Context ctx) {
        String basePath = StringUtils.removeEnd(ctx.matchedPath(), "*");
        basePath = StringUtils.removeStart(basePath, "/");
        String matchingPath = StringUtils.removeStart(ctx.path(), "/");

        return StringUtils.removeStart(matchingPath, basePath);
    }
}
