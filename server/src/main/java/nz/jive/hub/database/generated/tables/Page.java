/*
 * This file is generated by jOOQ.
 */
package nz.jive.hub.database.generated.tables;


import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import nz.jive.hub.database.generated.Keys;
import nz.jive.hub.database.generated.Public;
import nz.jive.hub.database.generated.tables.Organisation.OrganisationPath;
import nz.jive.hub.database.generated.tables.records.PageRecord;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.ForeignKey;
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
public class Page extends TableImpl<PageRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.page</code>
     */
    public static final Page PAGE = new Page();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PageRecord> getRecordType() {
        return PageRecord.class;
    }

    /**
     * The column <code>public.page.path</code>.
     */
    public final TableField<PageRecord, String> PATH = createField(DSL.name("path"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>public.page.title</code>.
     */
    public final TableField<PageRecord, String> TITLE = createField(DSL.name("title"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>public.page.content</code>.
     */
    public final TableField<PageRecord, String> CONTENT = createField(DSL.name("content"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>public.page.organisation_id</code>.
     */
    public final TableField<PageRecord, Integer> ORGANISATION_ID = createField(DSL.name("organisation_id"), SQLDataType.INTEGER.nullable(false), this, "");

    private Page(Name alias, Table<PageRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private Page(Name alias, Table<PageRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>public.page</code> table reference
     */
    public Page(String alias) {
        this(DSL.name(alias), PAGE);
    }

    /**
     * Create an aliased <code>public.page</code> table reference
     */
    public Page(Name alias) {
        this(alias, PAGE);
    }

    /**
     * Create a <code>public.page</code> table reference
     */
    public Page() {
        this(DSL.name("page"), null);
    }

    public <O extends Record> Page(Table<O> path, ForeignKey<O, PageRecord> childPath, InverseForeignKey<O, PageRecord> parentPath) {
        super(path, childPath, parentPath, PAGE);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class PagePath extends Page implements Path<PageRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> PagePath(Table<O> path, ForeignKey<O, PageRecord> childPath, InverseForeignKey<O, PageRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private PagePath(Name alias, Table<PageRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public PagePath as(String alias) {
            return new PagePath(DSL.name(alias), this);
        }

        @Override
        public PagePath as(Name alias) {
            return new PagePath(alias, this);
        }

        @Override
        public PagePath as(Table<?> alias) {
            return new PagePath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<PageRecord> getPrimaryKey() {
        return Keys.PAGE_PKEY;
    }

    @Override
    public List<ForeignKey<PageRecord, ?>> getReferences() {
        return Arrays.asList(Keys.PAGE__PAGE_ORGANISATION_ID_FKEY);
    }

    private transient OrganisationPath _organisation;

    /**
     * Get the implicit join path to the <code>public.organisation</code> table.
     */
    public OrganisationPath organisation() {
        if (_organisation == null)
            _organisation = new OrganisationPath(this, Keys.PAGE__PAGE_ORGANISATION_ID_FKEY, null);

        return _organisation;
    }

    @Override
    public Page as(String alias) {
        return new Page(DSL.name(alias), this);
    }

    @Override
    public Page as(Name alias) {
        return new Page(alias, this);
    }

    @Override
    public Page as(Table<?> alias) {
        return new Page(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Page rename(String name) {
        return new Page(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Page rename(Name name) {
        return new Page(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Page rename(Table<?> name) {
        return new Page(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Page where(Condition condition) {
        return new Page(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Page where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Page where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Page where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Page where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Page where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Page where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Page where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Page whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Page whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}