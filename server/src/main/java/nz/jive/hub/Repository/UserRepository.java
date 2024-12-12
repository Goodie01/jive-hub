package nz.jive.hub.Repository;

import nz.jive.hub.database.generated.tables.records.UserDetailRecord;
import org.jooq.Configuration;
import org.jooq.impl.DSL;

import java.util.Optional;

import static nz.jive.hub.database.generated.Tables.USER_DETAIL;

/**
 * @author thomas.goodwin
 */
public class UserRepository {
    public UserDetailRecord create(
            Configuration configuration,
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

    public Optional<UserDetailRecord> findByEmail(Configuration configuration, Integer organisationId, String email) {
        return DSL.using(configuration)
                .selectFrom(USER_DETAIL)
                .where(USER_DETAIL.ORGANISATION_ID.eq(organisationId).and(USER_DETAIL.EMAIL.eq(email)))
                .fetchOptional();
    }
}
