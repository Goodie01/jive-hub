package nz.jive.hub.service;

import org.jooq.Configuration;
import nz.jive.hub.database.generated.tables.records.UserDetailRecord;

/**
 * @author thomas.goodwin
 */
public class UserService {
    private final Configuration configuration;

    public UserService(Configuration configuration) {
        this.configuration = configuration;
    }

    public UserDetailRecord create(
        final String name,
        final String preferredName,
        final String email,
        final int organisationId
    ) {
        UserDetailRecord userDetailRecord = new UserDetailRecord();
        userDetailRecord.attach(configuration);
        userDetailRecord.setName(name);
        userDetailRecord.setPreferredName(preferredName);
        userDetailRecord.setEmail(email);
        userDetailRecord.setOrganisationId(organisationId);
        userDetailRecord.store();

        return userDetailRecord;
    }
}
