package nz.jive.hub.api.domain;

import java.time.OffsetDateTime;

/**
 * @author Goodie
 */
public record Event(int id, String displayName, String byLine, OffsetDateTime startTime, OffsetDateTime endTime) {
}
