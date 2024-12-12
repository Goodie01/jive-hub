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

    public void store(Context ctx, UserDetailRecord userDetailRecord) {
        ctx.attribute(ATTR_USER, userDetailRecord);
    }

    public void store(Context ctx, Policy policy) {
        ctx.attribute(ATTR_USER_POLICY, policy);
    }

    public void store(Context ctx, OrganisationRecord organisationRecord) {
        ctx.attribute(ATTR_ORGANISATION, organisationRecord);
    }

    public Optional<UserDetailRecord> userRecord(Context ctx) {
        return Optional.ofNullable(ctx.attribute(ATTR_USER));
    }

    public Optional<Policy> policy(Context ctx) {
        return Optional.ofNullable(ctx.attribute(ATTR_USER_POLICY));
    }

    public Optional<OrganisationRecord> organisation(Context ctx) {
        return Optional.ofNullable(ctx.attribute(ATTR_ORGANISATION));
    }
}
