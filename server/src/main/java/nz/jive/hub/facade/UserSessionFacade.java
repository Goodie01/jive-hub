package nz.jive.hub.facade;

import nz.jive.hub.database.DatabaseService;
import nz.jive.hub.database.Repository.UserRepository;
import nz.jive.hub.database.Repository.UserSessionRepository;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.database.generated.tables.records.UserDetailRecord;
import nz.jive.hub.database.generated.tables.records.UserSessionRecord;
import org.jooq.impl.DSL;

import java.util.Optional;

/**
 * @author Goodie
 */
public class UserSessionFacade {
    private final DatabaseService databaseService;
    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;

    public UserSessionFacade(DatabaseService databaseService, UserRepository userRepository, UserSessionRepository userSessionRepository) {
        this.databaseService = databaseService;
        this.userRepository = userRepository;
        this.userSessionRepository = userSessionRepository;
    }

    public UserSessionRecord createSession(OrganisationRecord organisation, String email) {
        return DSL.using(databaseService.getConfiguration())
                .transactionResult(configuration -> {
                    UserDetailRecord userDetailRecord = userRepository.findByEmail(configuration, organisation.getId(), email).orElseThrow();

                    return userSessionRepository.create(configuration, organisation.getId(), userDetailRecord.getId());
                });
    }

    public Optional<UserDetailRecord> findFromSessionKey(final OrganisationRecord organisation, final String authHeader) {
        return userSessionRepository.findFromSessionKey(databaseService.getConfiguration(), organisation, authHeader);
    }


}
