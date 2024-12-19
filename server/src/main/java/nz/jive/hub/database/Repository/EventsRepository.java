package nz.jive.hub.database.Repository;

import nz.jive.hub.database.generated.tables.records.EventRecord;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import org.jooq.Configuration;
import org.jooq.impl.DSL;

import java.time.OffsetDateTime;
import java.util.List;

import static nz.jive.hub.database.generated.Tables.EVENT;

/**
 * @author Goodie
 */
public class EventsRepository {
    public List<EventRecord> getEvents(Configuration configuration, final int organisationId) {
        return DSL.using(configuration)
                .selectFrom(EVENT)
                .where(EVENT.ORGANISATION_ID.eq(organisationId))
                .stream()
                .toList();
    }

    public EventRecord createEvent(Configuration configuration,
                                   OrganisationRecord organisation,
                                   String name,
                                   String byLine,
                                   OffsetDateTime startTime,
                                   OffsetDateTime endTime) {
        EventRecord eventRecord = new EventRecord();
        eventRecord.attach(configuration);
        eventRecord.setOrganisationId(organisation.getId());
        eventRecord.setDisplayName(name);
        eventRecord.setByLine(byLine);
        eventRecord.setStartDate(startTime);
        eventRecord.setEndDate(endTime);
        eventRecord.store();

        return eventRecord;
    }
}
