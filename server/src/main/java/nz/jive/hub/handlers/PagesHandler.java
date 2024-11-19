package nz.jive.hub.handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import nz.jive.hub.api.PageResp;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.database.generated.tables.records.PageRecord;
import nz.jive.hub.service.PageService;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * @author Goodie
 */
public class PagesHandler implements Handler {
    private final PageService pageService;

    public PagesHandler(PageService pageService) {
        this.pageService = pageService;
    }

    public static PagesHandler create(final PageService pageService) {
        return new PagesHandler(pageService);
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        //Object o = ctx.bodyAsClass(Object.class);
        OrganisationRecord organisation = ctx.attribute("organisation");
        String pageId = extractPageId(ctx);

        PageRecord pageRecord = pageService
            .find(organisation.getId(), pageId)
            .orElseThrow();

        ctx.json(new PageResp(pageRecord.getContent(), pageRecord.getTitle()));
    }

    @NotNull
    private static String extractPageId(@NotNull Context ctx) {
        String basePath = StringUtils.removeEnd(ctx.matchedPath(), "*");
        basePath = StringUtils.removeStart(basePath, "/");
        String matchingPath = StringUtils.removeStart(ctx.path(), "/");

        return StringUtils.removeStart(matchingPath, basePath);
    }
}
