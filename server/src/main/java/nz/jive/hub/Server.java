package nz.jive.hub;

import io.javalin.http.Context;
import nz.jive.hub.database.DatabaseService;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.service.SecurityService;
import nz.jive.hub.service.security.Policy;
import org.jooq.Configuration;
import org.jooq.impl.DSL;

import static nz.jive.hub.database.generated.Tables.*;

/**
 * @author Goodie
 */
public class Server {
    public static final String ATTR_ORGANISATION = "organisation";
    public static final String ATTR_USER = "user";
    public static final String ATTR_USER_POLICY = "userPolicy";

    public static void setup(Context ctx, DatabaseService databaseService, SecurityService securityService) {
        String host = ctx.header("host");
        Configuration configuration = databaseService.getConfiguration();

        DSL
                .using(configuration)
                .select(PARAMETERS.ORGANISATION_ID)
                .from(PARAMETERS)
                .where(PARAMETERS.VALUE.contains(host))
                .and(PARAMETERS.PARAMETER_NAME.eq(Parameters.ORGANISATION_HOSTS.getName()))
                .fetchOptionalInto(Integer.class)
                .ifPresent(id -> {
                    OrganisationRecord organisation = DSL
                            .using(configuration)
                            .selectFrom(ORGANISATION)
                            .where(ORGANISATION.ID.equal(id))
                            .fetchOne();
                    ctx.attribute(ATTR_ORGANISATION, organisation);

                    if (organisation != null) {
                        String authHeader = ctx.header("authorization");

                        DSL.using(configuration)
                                .selectFrom(USER_DETAIL)
                                .where(
                                        USER_DETAIL.ORGANISATION_ID.equal(organisation.getId())
                                                .and(USER_DETAIL.ID.in(
                                                        DSL.select(USER_SESSION.ID).from(USER_SESSION).where(USER_SESSION.SESSION_KEY.eq(authHeader)))
                                                )
                                )
                                .fetchOptional()
                                .ifPresentOrElse(userDetailRecord -> {
                                            ctx.attribute(ATTR_USER, userDetailRecord);

                                            Policy combinedRoleForUser = securityService.getCombinedRoleForUser(userDetailRecord);
                                            ctx.attribute(ATTR_USER_POLICY, combinedRoleForUser);
                                        },
                                        () -> {
                                            Policy combinedRoleForUser = securityService.getDenyAllPolicy();
                                            ctx.attribute(ATTR_USER_POLICY, combinedRoleForUser);
                                        });
                    }
                });
    }
}
