/*
 * This file is generated by jOOQ.
 */
package nz.jive.hub.database.generated.tables.records;


import java.time.OffsetDateTime;

import nz.jive.hub.database.generated.tables.Event;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class EventRecord extends UpdatableRecordImpl<EventRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.event.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.event.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.event.organisation_id</code>.
     */
    public void setOrganisationId(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.event.organisation_id</code>.
     */
    public Integer getOrganisationId() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>public.event.display_name</code>.
     */
    public void setDisplayName(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.event.display_name</code>.
     */
    public String getDisplayName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.event.by_line</code>.
     */
    public void setByLine(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.event.by_line</code>.
     */
    public String getByLine() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.event.start_date</code>.
     */
    public void setStartDate(OffsetDateTime value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.event.start_date</code>.
     */
    public OffsetDateTime getStartDate() {
        return (OffsetDateTime) get(4);
    }

    /**
     * Setter for <code>public.event.end_date</code>.
     */
    public void setEndDate(OffsetDateTime value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.event.end_date</code>.
     */
    public OffsetDateTime getEndDate() {
        return (OffsetDateTime) get(5);
    }

    /**
     * Setter for <code>public.event.created_date</code>.
     */
    public void setCreatedDate(OffsetDateTime value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.event.created_date</code>.
     */
    public OffsetDateTime getCreatedDate() {
        return (OffsetDateTime) get(6);
    }

    /**
     * Setter for <code>public.event.last_updated_date</code>.
     */
    public void setLastUpdatedDate(OffsetDateTime value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.event.last_updated_date</code>.
     */
    public OffsetDateTime getLastUpdatedDate() {
        return (OffsetDateTime) get(7);
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
     * Create a detached EventRecord
     */
    public EventRecord() {
        super(Event.EVENT);
    }

    /**
     * Create a detached, initialised EventRecord
     */
    public EventRecord(Integer id, Integer organisationId, String displayName, String byLine, OffsetDateTime startDate, OffsetDateTime endDate, OffsetDateTime createdDate, OffsetDateTime lastUpdatedDate) {
        super(Event.EVENT);

        setId(id);
        setOrganisationId(organisationId);
        setDisplayName(displayName);
        setByLine(byLine);
        setStartDate(startDate);
        setEndDate(endDate);
        setCreatedDate(createdDate);
        setLastUpdatedDate(lastUpdatedDate);
        resetChangedOnNotNull();
    }
}