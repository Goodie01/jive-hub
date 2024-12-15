/*
 * This file is generated by jOOQ.
 */
package nz.jive.hub.database.generated.tables;


import java.time.OffsetDateTime;
import java.util.Collection;

import nz.jive.hub.database.generated.Keys;
import nz.jive.hub.database.generated.Public;
import nz.jive.hub.database.generated.tables.HostNames.HostNamesPath;
import nz.jive.hub.database.generated.tables.Page.PagePath;
import nz.jive.hub.database.generated.tables.Parameters.ParametersPath;
import nz.jive.hub.database.generated.tables.Role.RolePath;
import nz.jive.hub.database.generated.tables.UserDetail.UserDetailPath;
import nz.jive.hub.database.generated.tables.UserSession.UserSessionPath;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.InverseForeignKey;
import org.jooq.Name;
import org.jooq.Path;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Organisation extends TableImpl<OrganisationRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.organisation</code>
     */
    public static final Organisation ORGANISATION = new Organisation();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<OrganisationRecord> getRecordType() {
        return OrganisationRecord.class;
    }

    /**
     * The column <code>public.organisation.id</code>.
     */
    public final TableField<OrganisationRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.organisation.display_name</code>.
     */
    public final TableField<OrganisationRecord, String> DISPLAY_NAME = createField(DSL.name("display_name"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>public.organisation.created_date</code>.
     */
    public final TableField<OrganisationRecord, OffsetDateTime> CREATED_DATE = createField(DSL.name("created_date"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).nullable(false), this, "");

    /**
     * The column <code>public.organisation.last_updated_date</code>.
     */
    public final TableField<OrganisationRecord, OffsetDateTime> LAST_UPDATED_DATE = createField(DSL.name("last_updated_date"), SQLDataType.TIMESTAMPWITHTIMEZONE(6), this, "");

    private Organisation(Name alias, Table<OrganisationRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private Organisation(Name alias, Table<OrganisationRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>public.organisation</code> table reference
     */
    public Organisation(String alias) {
        this(DSL.name(alias), ORGANISATION);
    }

    /**
     * Create an aliased <code>public.organisation</code> table reference
     */
    public Organisation(Name alias) {
        this(alias, ORGANISATION);
    }

    /**
     * Create a <code>public.organisation</code> table reference
     */
    public Organisation() {
        this(DSL.name("organisation"), null);
    }

    public <O extends Record> Organisation(Table<O> path, ForeignKey<O, OrganisationRecord> childPath, InverseForeignKey<O, OrganisationRecord> parentPath) {
        super(path, childPath, parentPath, ORGANISATION);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class OrganisationPath extends Organisation implements Path<OrganisationRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> OrganisationPath(Table<O> path, ForeignKey<O, OrganisationRecord> childPath, InverseForeignKey<O, OrganisationRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private OrganisationPath(Name alias, Table<OrganisationRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public OrganisationPath as(String alias) {
            return new OrganisationPath(DSL.name(alias), this);
        }

        @Override
        public OrganisationPath as(Name alias) {
            return new OrganisationPath(alias, this);
        }

        @Override
        public OrganisationPath as(Table<?> alias) {
            return new OrganisationPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<OrganisationRecord, Integer> getIdentity() {
        return (Identity<OrganisationRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<OrganisationRecord> getPrimaryKey() {
        return Keys.ORGANISATION_PKEY;
    }

    private transient HostNamesPath _hostNames;

    /**
     * Get the implicit to-many join path to the <code>public.host_names</code>
     * table
     */
    public HostNamesPath hostNames() {
        if (_hostNames == null)
            _hostNames = new HostNamesPath(this, null, Keys.HOST_NAMES__HOST_NAMES_ORGANISATION_ID_FKEY.getInverseKey());

        return _hostNames;
    }

    private transient PagePath _page;

    /**
     * Get the implicit to-many join path to the <code>public.page</code> table
     */
    public PagePath page() {
        if (_page == null)
            _page = new PagePath(this, null, Keys.PAGE__PAGE_ORGANISATION_ID_FKEY.getInverseKey());

        return _page;
    }

    private transient ParametersPath _parameters;

    /**
     * Get the implicit to-many join path to the <code>public.parameters</code>
     * table
     */
    public ParametersPath parameters() {
        if (_parameters == null)
            _parameters = new ParametersPath(this, null, Keys.PARAMETERS__PARAMETERS_ORGANISATION_ID_FKEY.getInverseKey());

        return _parameters;
    }

    private transient RolePath _role;

    /**
     * Get the implicit to-many join path to the <code>public.role</code> table
     */
    public RolePath role() {
        if (_role == null)
            _role = new RolePath(this, null, Keys.ROLE__ROLE_ORGANISATION_ID_FKEY.getInverseKey());

        return _role;
    }

    private transient UserDetailPath _userDetail;

    /**
     * Get the implicit to-many join path to the <code>public.user_detail</code>
     * table
     */
    public UserDetailPath userDetail() {
        if (_userDetail == null)
            _userDetail = new UserDetailPath(this, null, Keys.USER_DETAIL__USER_DETAIL_ORGANISATION_ID_FKEY.getInverseKey());

        return _userDetail;
    }

    private transient UserSessionPath _userSession;

    /**
     * Get the implicit to-many join path to the
     * <code>public.user_session</code> table
     */
    public UserSessionPath userSession() {
        if (_userSession == null)
            _userSession = new UserSessionPath(this, null, Keys.USER_SESSION__USER_SESSION_ORGANISATION_ID_FKEY.getInverseKey());

        return _userSession;
    }

    @Override
    public Organisation as(String alias) {
        return new Organisation(DSL.name(alias), this);
    }

    @Override
    public Organisation as(Name alias) {
        return new Organisation(alias, this);
    }

    @Override
    public Organisation as(Table<?> alias) {
        return new Organisation(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Organisation rename(String name) {
        return new Organisation(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Organisation rename(Name name) {
        return new Organisation(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Organisation rename(Table<?> name) {
        return new Organisation(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Organisation where(Condition condition) {
        return new Organisation(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Organisation where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Organisation where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Organisation where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Organisation where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Organisation where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Organisation where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Organisation where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Organisation whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Organisation whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
