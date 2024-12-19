package nz.jive.hub.database.Repository;

import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import org.jooq.Configuration;
import org.jooq.impl.DSL;

import static nz.jive.hub.database.generated.Tables.HOST_NAMES;

/**
 * @author Goodie
 */
public class HostsRepository {
    public void addHostName(Configuration configuration, OrganisationRecord organisationRecord, String hostName) {
        DSL.using(configuration)
                .insertInto(HOST_NAMES)
                .columns(HOST_NAMES.HOST, HOST_NAMES.ORGANISATION_ID)
                .values(hostName, organisationRecord.getId())
                .execute();
    }
}
