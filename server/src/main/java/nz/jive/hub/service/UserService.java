package nz.jive.hub.service;

import nz.jive.hub.database.generated.tables.records.UserDetailRecord;
import org.jooq.Configuration;
import org.jooq.impl.DSL;

import java.util.Optional;

import static nz.jive.hub.database.generated.Tables.USER_DETAIL;

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

    public Optional<UserDetailRecord> findByEmail(Integer organisationId, String email) {
        return DSL.using(configuration)
                .deleteFrom(USER_DETAIL)
                .where(USER_DETAIL.ORGANISATION_ID.eq(organisationId).and(USER_DETAIL.EMAIL.eq(email)))
                .returning()
                .fetchOptional();
    }
}
