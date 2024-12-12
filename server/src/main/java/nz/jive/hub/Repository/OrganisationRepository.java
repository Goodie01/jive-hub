package nz.jive.hub.Repository;

import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import org.jooq.Configuration;
import org.jooq.impl.DSL;

import java.util.Optional;

import static nz.jive.hub.database.generated.Tables.HOST_NAMES;
import static nz.jive.hub.database.generated.Tables.ORGANISATION;

/**
 * @author Goodie
 */
public class OrganisationRepository {
    public OrganisationRecord createOrganisation(final Configuration configuration, String slug, final String orgDisplayName) {
        OrganisationRecord organisationRecord = new OrganisationRecord();
        organisationRecord.attach(configuration);
        organisationRecord.setSlug(slug);
        organisationRecord.setDisplayName(orgDisplayName);
        organisationRecord.store();

        return organisationRecord;
    }

    public Optional<OrganisationRecord> findFromHostName(final Configuration configuration, String host) {
        return DSL.using(configuration)
                .selectFrom(ORGANISATION)
                .where(ORGANISATION.ID.in(
                        DSL.select(HOST_NAMES.ORGANISATION_ID)
                                .from(HOST_NAMES)
                                .where(HOST_NAMES.HOST.equalIgnoreCase(host))
                ))
                .fetchOptional();
    }
}
