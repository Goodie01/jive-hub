package nz.jive.hub.service;

import nz.jive.hub.database.generated.tables.records.UserSessionRecord;
import org.jooq.Configuration;
import org.jooq.impl.DSL;

import java.util.Optional;
import java.util.UUID;

import static nz.jive.hub.database.generated.Tables.USER_SESSION;

/**
 * @author Goodie
 */
public class UserSessionService {
    private final Configuration configuration;

    public UserSessionService(Configuration configuration) {
        this.configuration = configuration;
    }

    public UserSessionRecord create(int organisationId, int userId) {
        UserSessionRecord userSessionRecord = new UserSessionRecord();
        userSessionRecord.attach(configuration);
        userSessionRecord.setUserId(userId);
        userSessionRecord.setOrganisationId(organisationId);
        userSessionRecord.setSessionKey(UUID.randomUUID().toString());
        userSessionRecord.store();
        return userSessionRecord;
    }

    public Optional<UserSessionRecord> findByKey(int organisationId, String key) {
        return DSL.using(configuration)
                .selectFrom(USER_SESSION)
                .where(USER_SESSION.SESSION_KEY.eq(key))
                .and(USER_SESSION.ORGANISATION_ID.eq(organisationId))
                .fetchOptional();
    }
}
