/*
 * This file is generated by jOOQ.
 */
package nz.jive.hub.database.generated;


import java.util.Arrays;
import java.util.List;

import nz.jive.hub.database.generated.tables.Event;
import nz.jive.hub.database.generated.tables.FlywaySchemaHistory;
import nz.jive.hub.database.generated.tables.HostNames;
import nz.jive.hub.database.generated.tables.Organisation;
import nz.jive.hub.database.generated.tables.Page;
import nz.jive.hub.database.generated.tables.Parameters;
import nz.jive.hub.database.generated.tables.Role;
import nz.jive.hub.database.generated.tables.UserDetail;
import nz.jive.hub.database.generated.tables.UserHasRole;
import nz.jive.hub.database.generated.tables.UserSession;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Public extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public</code>
     */
    public static final Public PUBLIC = new Public();

    /**
     * The table <code>public.event</code>.
     */
    public final Event EVENT = Event.EVENT;

    /**
     * The table <code>public.flyway_schema_history</code>.
     */
    public final FlywaySchemaHistory FLYWAY_SCHEMA_HISTORY = FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY;

    /**
     * The table <code>public.host_names</code>.
     */
    public final HostNames HOST_NAMES = HostNames.HOST_NAMES;

    /**
     * The table <code>public.organisation</code>.
     */
    public final Organisation ORGANISATION = Organisation.ORGANISATION;

    /**
     * The table <code>public.page</code>.
     */
    public final Page PAGE = Page.PAGE;

    /**
     * The table <code>public.parameters</code>.
     */
    public final Parameters PARAMETERS = Parameters.PARAMETERS;

    /**
     * The table <code>public.role</code>.
     */
    public final Role ROLE = Role.ROLE;

    /**
     * The table <code>public.user_detail</code>.
     */
    public final UserDetail USER_DETAIL = UserDetail.USER_DETAIL;

    /**
     * The table <code>public.user_has_role</code>.
     */
    public final UserHasRole USER_HAS_ROLE = UserHasRole.USER_HAS_ROLE;

    /**
     * The table <code>public.user_session</code>.
     */
    public final UserSession USER_SESSION = UserSession.USER_SESSION;

    /**
     * No further instances allowed
     */
    private Public() {
        super("public", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            Event.EVENT,
            FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY,
            HostNames.HOST_NAMES,
            Organisation.ORGANISATION,
            Page.PAGE,
            Parameters.PARAMETERS,
            Role.ROLE,
            UserDetail.USER_DETAIL,
            UserHasRole.USER_HAS_ROLE,
            UserSession.USER_SESSION
        );
    }
}
