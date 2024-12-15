package nz.jive.hub.facade;

import io.javalin.http.Context;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.database.generated.tables.records.UserDetailRecord;
import nz.jive.hub.service.security.Policy;

import java.util.Optional;

/**
 * @author Goodie
 */
public class SessionFacade {
    private static final String ATTR_ORGANISATION = "organisation";
    private static final String ATTR_USER = "user";
    private static final String ATTR_USER_POLICY = "userPolicy";
    private final Context ctx;

    public SessionFacade(Context ctx) {
        this.ctx = ctx;
    }

    public void store(UserDetailRecord userDetailRecord) {
        this.ctx.attribute(ATTR_USER, userDetailRecord);
    }

    public void store(Policy policy) {
        this.ctx.attribute(ATTR_USER_POLICY, policy);
    }

    public void store(OrganisationRecord organisationRecord) {
        this.ctx.attribute(ATTR_ORGANISATION, organisationRecord);
    }

    public Optional<UserDetailRecord> userRecord() {
        return Optional.ofNullable(this.ctx.attribute(ATTR_USER));
    }

    public Optional<Policy> policy() {
        return Optional.ofNullable(this.ctx.attribute(ATTR_USER_POLICY));
    }

    public Optional<OrganisationRecord> organisation() {
        return Optional.ofNullable(this.ctx.attribute(ATTR_ORGANISATION));
    }
}
