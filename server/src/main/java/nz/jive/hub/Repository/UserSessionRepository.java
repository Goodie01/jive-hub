package nz.jive.hub.Repository;

import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.database.generated.tables.records.UserDetailRecord;
import nz.jive.hub.database.generated.tables.records.UserSessionRecord;
import org.jooq.Configuration;
import org.jooq.impl.DSL;

import java.util.Optional;
import java.util.UUID;

import static nz.jive.hub.database.generated.Tables.USER_DETAIL;
import static nz.jive.hub.database.generated.Tables.USER_SESSION;

/**
 * @author Goodie
 */
public class UserSessionRepository {

    public UserSessionRecord create(Configuration configuration, int organisationId, int userId) {
        UserSessionRecord userSessionRecord = new UserSessionRecord();
        userSessionRecord.attach(configuration);
        userSessionRecord.setUserId(userId);
        userSessionRecord.setOrganisationId(organisationId);
        userSessionRecord.setSessionKey(UUID.randomUUID().toString());
        userSessionRecord.store();
        return userSessionRecord;
    }

    public Optional<UserDetailRecord> findFromSessionKey(Configuration configuration, final OrganisationRecord organisation, final String authHeader) {
        return DSL.using(configuration)
                .selectFrom(USER_DETAIL)
                .where(
                        USER_DETAIL.ORGANISATION_ID.equal(organisation.getId())
                                .and(USER_DETAIL.ID.in(
                                        DSL.select(USER_SESSION.USER_ID).from(USER_SESSION).where(USER_SESSION.SESSION_KEY.eq(authHeader)))
                                )
                )
                .fetchOptional();
    }
}
