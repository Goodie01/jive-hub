package nz.jive.hub.handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import nz.jive.hub.Server;
import nz.jive.hub.api.PageResp;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.service.PageService;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Goodie
 */
public class PagesHandler implements Handler {
    private final PageService pageService;

    public PagesHandler(PageService pageService) {
        this.pageService = pageService;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        //Object o = ctx.bodyAsClass(Object.class);
        OrganisationRecord organisation = Objects.requireNonNull(ctx.attribute(Server.ATTR_ORGANISATION));
        String pageId = extractPageId(ctx);

        pageService
                .find(organisation.getId(), pageId)
                .ifPresentOrElse(pageRecord -> {
                    ctx.json(new PageResp(pageRecord.getContent(), pageRecord.getTitle()));
                }, () -> {
                    ctx.json(new PageResp("Page Missing", "404 Not Found"));
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
