package nz.jive.hub.service.security;

import nz.jive.hub.database.generated.tables.records.OrganisationRecord;

/**
 * @author Goodie
 */
public final class SecurityUtils {
    private SecurityUtils() {
    }

    public static String namespace(final OrganisationRecord organisation) {
        return "organisation#" + organisation.getId();
    }
}
