package nz.jive.hub.database.Repository;

import nz.jive.hub.api.MenuItem;
import nz.jive.hub.database.generated.tables.records.PageRecord;
import org.jooq.Configuration;
import org.jooq.impl.DSL;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static nz.jive.hub.database.generated.Tables.PAGE;

/**
 * @author thomas.goodwin
 */
public class PageRepository {
    public void save(Configuration configuration, final int organisationId, final String path, final String content, final String title) {
        save(configuration, organisationId, path, content, title, null, 0);
    }

    public void save(Configuration configuration, final int organisationId, final String path, final String content, final String title, String menuName, int menuOrder) {
        PageRecord pageRecord = new PageRecord();
        pageRecord.attach(configuration);
        pageRecord.setOrganisationId(organisationId);
        pageRecord.setPath(path);
        pageRecord.setTitle(title);
        pageRecord.setContent(content);
        pageRecord.setMenuname(menuName);
        pageRecord.setMenuorder((short) menuOrder);
        pageRecord.store();
    }

    public Set<MenuItem> findMenuPages(Configuration configuration, final int organisationId) {
        return DSL.using(configuration)
                .select(PAGE.MENUNAME, PAGE.PATH, PAGE.MENUORDER)
                .from(PAGE)
                .where(PAGE.ORGANISATION_ID.eq(organisationId))
                .and(PAGE.MENUNAME.isNotNull())
                .orderBy(PAGE.MENUORDER)
                .stream()
                .map(menuItemPath -> new MenuItem(menuItemPath.component1(), menuItemPath.component2(), menuItemPath.component3()))
                .collect(Collectors.toSet());
    }

    public Optional<PageRecord> find(Configuration configuration, final int organisationId, final String path) {
        return DSL.using(configuration)
                .selectFrom(PAGE)
                .where(PAGE.ORGANISATION_ID
                        .eq(organisationId)
                        .and(PAGE.PATH.eq(path)))
                .fetchOptional();
    }
}
