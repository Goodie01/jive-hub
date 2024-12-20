/*
 * This file is generated by jOOQ.
 */
package nz.jive.hub.database.generated.tables.records;


import nz.jive.hub.database.generated.tables.UserSession;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class UserSessionRecord extends UpdatableRecordImpl<UserSessionRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.user_session.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.user_session.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.user_session.organisation_id</code>.
     */
    public void setOrganisationId(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.user_session.organisation_id</code>.
     */
    public Integer getOrganisationId() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>public.user_session.user_id</code>.
     */
    public void setUserId(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.user_session.user_id</code>.
     */
    public Integer getUserId() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>public.user_session.session_key</code>.
     */
    public void setSessionKey(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.user_session.session_key</code>.
     */
    public String getSessionKey() {
        return (String) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached UserSessionRecord
     */
    public UserSessionRecord() {
        super(UserSession.USER_SESSION);
    }

    /**
     * Create a detached, initialised UserSessionRecord
     */
    public UserSessionRecord(Integer id, Integer organisationId, Integer userId, String sessionKey) {
        super(UserSession.USER_SESSION);

        setId(id);
        setOrganisationId(organisationId);
        setUserId(userId);
        setSessionKey(sessionKey);
        resetChangedOnNotNull();
    }
}
