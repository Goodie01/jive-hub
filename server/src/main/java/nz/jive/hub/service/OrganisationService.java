package nz.jive.hub.service;

import java.util.regex.Pattern;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import org.jooq.Configuration;

/**
 * @author Goodie
 */
public class OrganisationService {
    private final Configuration configuration;

    public OrganisationService(Configuration configuration) {
        this.configuration = configuration;
    }

    public OrganisationRecord createOrganisation(final String orgDisplayName) {
        OrganisationRecord organisationRecord = new OrganisationRecord();
        organisationRecord.attach(configuration);
        organisationRecord.setDisplayName(orgDisplayName);
        organisationRecord.store();

        return organisationRecord;
    }
}
