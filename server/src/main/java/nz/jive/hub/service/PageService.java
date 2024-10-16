package nz.jive.hub.service;

import nz.jive.hub.database.generated.tables.records.PageRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.Configuration;
import org.jooq.impl.DSL;

import static nz.jive.hub.database.generated.Tables.PAGE;

import java.util.Optional;

/**
 * @author thomas.goodwin
 */
public class PageService {
    private final Configuration configuration;

    public PageService(Configuration configuration) {
        this.configuration = configuration;
    }

    public void save(final String path, final String content, final String title, final int organisationId) {
        PageRecord pageRecord = new PageRecord();
        pageRecord.attach(configuration);
        pageRecord.setOrganisationId(organisationId);
        pageRecord.setPath(path);
        pageRecord.setTitle(title);
        pageRecord.setContent(content);
        pageRecord.store();
    }

    public Optional<PageRecord> find(final String path, final int organisationId) {
        return DSL.using(configuration)
            .selectFrom(PAGE)
            .where(PAGE.ORGANISATION_ID
                .eq(organisationId)
                .and(PAGE.PATH.eq(path)))
            .fetchOptional();
    }
}
