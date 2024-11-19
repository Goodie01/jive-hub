/*
 * This file is generated by jOOQ.
 */
package nz.jive.hub.database.generated;


import nz.jive.hub.database.generated.tables.FlywaySchemaHistory;
import nz.jive.hub.database.generated.tables.Organisation;
import nz.jive.hub.database.generated.tables.Page;
import nz.jive.hub.database.generated.tables.Parameters;
import nz.jive.hub.database.generated.tables.Role;
import nz.jive.hub.database.generated.tables.UserDetail;
import nz.jive.hub.database.generated.tables.UserHasRole;
import nz.jive.hub.database.generated.tables.records.FlywaySchemaHistoryRecord;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.database.generated.tables.records.PageRecord;
import nz.jive.hub.database.generated.tables.records.ParametersRecord;
import nz.jive.hub.database.generated.tables.records.RoleRecord;
import nz.jive.hub.database.generated.tables.records.UserDetailRecord;
import nz.jive.hub.database.generated.tables.records.UserHasRoleRecord;

import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<FlywaySchemaHistoryRecord> FLYWAY_SCHEMA_HISTORY_PK = Internal.createUniqueKey(FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY, DSL.name("flyway_schema_history_pk"), new TableField[] { FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.INSTALLED_RANK }, true);
    public static final UniqueKey<OrganisationRecord> ORGANISATION_PKEY = Internal.createUniqueKey(Organisation.ORGANISATION, DSL.name("organisation_pkey"), new TableField[] { Organisation.ORGANISATION.ID }, true);
    public static final UniqueKey<PageRecord> PAGE_ORGANISATION_ID_MENUNAME_KEY = Internal.createUniqueKey(Page.PAGE, DSL.name("page_organisation_id_menuname_key"), new TableField[] { Page.PAGE.ORGANISATION_ID, Page.PAGE.MENUNAME }, true);
    public static final UniqueKey<PageRecord> PAGE_PKEY = Internal.createUniqueKey(Page.PAGE, DSL.name("page_pkey"), new TableField[] { Page.PAGE.ORGANISATION_ID, Page.PAGE.PATH }, true);
    public static final UniqueKey<ParametersRecord> PARAMETERS_ORGANISATION_ID_USER_ID_PARAMETER_NAME_KEY = Internal.createUniqueKey(Parameters.PARAMETERS, DSL.name("parameters_organisation_id_user_id_parameter_name_key"), new TableField[] { Parameters.PARAMETERS.ORGANISATION_ID, Parameters.PARAMETERS.USER_ID, Parameters.PARAMETERS.PARAMETER_NAME }, true);
    public static final UniqueKey<ParametersRecord> PARAMETERS_PKEY = Internal.createUniqueKey(Parameters.PARAMETERS, DSL.name("parameters_pkey"), new TableField[] { Parameters.PARAMETERS.ID }, true);
    public static final UniqueKey<RoleRecord> ROLE_PKEY = Internal.createUniqueKey(Role.ROLE, DSL.name("role_pkey"), new TableField[] { Role.ROLE.ORGANISATION_ID, Role.ROLE.NAME }, true);
    public static final UniqueKey<UserDetailRecord> USER_DETAIL_PKEY = Internal.createUniqueKey(UserDetail.USER_DETAIL, DSL.name("user_detail_pkey"), new TableField[] { UserDetail.USER_DETAIL.ID }, true);
    public static final UniqueKey<UserHasRoleRecord> USER_HAS_ROLE_PKEY = Internal.createUniqueKey(UserHasRole.USER_HAS_ROLE, DSL.name("user_has_role_pkey"), new TableField[] { UserHasRole.USER_HAS_ROLE.ORGANISATION_ID, UserHasRole.USER_HAS_ROLE.ROLE_NAME, UserHasRole.USER_HAS_ROLE.USER_ID }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<PageRecord, OrganisationRecord> PAGE__PAGE_ORGANISATION_ID_FKEY = Internal.createForeignKey(Page.PAGE, DSL.name("page_organisation_id_fkey"), new TableField[] { Page.PAGE.ORGANISATION_ID }, Keys.ORGANISATION_PKEY, new TableField[] { Organisation.ORGANISATION.ID }, true);
    public static final ForeignKey<ParametersRecord, OrganisationRecord> PARAMETERS__PARAMETERS_ORGANISATION_ID_FKEY = Internal.createForeignKey(Parameters.PARAMETERS, DSL.name("parameters_organisation_id_fkey"), new TableField[] { Parameters.PARAMETERS.ORGANISATION_ID }, Keys.ORGANISATION_PKEY, new TableField[] { Organisation.ORGANISATION.ID }, true);
    public static final ForeignKey<ParametersRecord, UserDetailRecord> PARAMETERS__PARAMETERS_USER_ID_FKEY = Internal.createForeignKey(Parameters.PARAMETERS, DSL.name("parameters_user_id_fkey"), new TableField[] { Parameters.PARAMETERS.USER_ID }, Keys.USER_DETAIL_PKEY, new TableField[] { UserDetail.USER_DETAIL.ID }, true);
    public static final ForeignKey<RoleRecord, OrganisationRecord> ROLE__ROLE_ORGANISATION_ID_FKEY = Internal.createForeignKey(Role.ROLE, DSL.name("role_organisation_id_fkey"), new TableField[] { Role.ROLE.ORGANISATION_ID }, Keys.ORGANISATION_PKEY, new TableField[] { Organisation.ORGANISATION.ID }, true);
    public static final ForeignKey<UserDetailRecord, OrganisationRecord> USER_DETAIL__USER_DETAIL_ORGANISATION_ID_FKEY = Internal.createForeignKey(UserDetail.USER_DETAIL, DSL.name("user_detail_organisation_id_fkey"), new TableField[] { UserDetail.USER_DETAIL.ORGANISATION_ID }, Keys.ORGANISATION_PKEY, new TableField[] { Organisation.ORGANISATION.ID }, true);
    public static final ForeignKey<UserHasRoleRecord, RoleRecord> USER_HAS_ROLE__USER_HAS_ROLE_ORGANISATION_ID_ROLE_NAME_FKEY = Internal.createForeignKey(UserHasRole.USER_HAS_ROLE, DSL.name("user_has_role_organisation_id_role_name_fkey"), new TableField[] { UserHasRole.USER_HAS_ROLE.ORGANISATION_ID, UserHasRole.USER_HAS_ROLE.ROLE_NAME }, Keys.ROLE_PKEY, new TableField[] { Role.ROLE.ORGANISATION_ID, Role.ROLE.NAME }, true);
    public static final ForeignKey<UserHasRoleRecord, UserDetailRecord> USER_HAS_ROLE__USER_HAS_ROLE_USER_ID_FKEY = Internal.createForeignKey(UserHasRole.USER_HAS_ROLE, DSL.name("user_has_role_user_id_fkey"), new TableField[] { UserHasRole.USER_HAS_ROLE.USER_ID }, Keys.USER_DETAIL_PKEY, new TableField[] { UserDetail.USER_DETAIL.ID }, true);
}
