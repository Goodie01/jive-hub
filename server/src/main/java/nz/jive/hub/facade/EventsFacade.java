package nz.jive.hub.facade;

import nz.jive.hub.Parameters;
import nz.jive.hub.database.DatabaseService;
import nz.jive.hub.database.Repository.EventsRepository;
import nz.jive.hub.database.Repository.ParameterStoreRepository;
import nz.jive.hub.database.Repository.parameters.ParameterMap;
import nz.jive.hub.database.generated.tables.records.EventRecord;
import nz.jive.hub.database.generated.tables.records.OrganisationRecord;
import nz.jive.hub.service.server.ServerContext;
import org.jooq.Configuration;
import org.jooq.impl.DSL;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author Goodie
 */
public class EventsFacade {

    private final ParameterStoreRepository parameterStoreRepository;
    private final DatabaseService databaseService;
    private final EventsRepository eventsRepository;

    public EventsFacade(DatabaseService databaseService, ParameterStoreRepository parameterStoreRepository, EventsRepository eventsRepository) {
        this.parameterStoreRepository = parameterStoreRepository;
        this.databaseService = databaseService;
        this.eventsRepository = eventsRepository;
    }

    public List<EventRecord> findAll(ServerContext serverContext) {
        OrganisationRecord organisation = serverContext.organisation();
        Configuration configuration = databaseService.getConfiguration();
        ParameterMap organizationParameters = parameterStoreRepository.getOrganizationParameters(configuration, organisation.getId());

        if (!organizationParameters.boolVal(Parameters.EVENT_ENABLED)) {
            return List.of();
        }

        return eventsRepository.getEvents(configuration, organisation.getId());
    }

    public void createEvent(OrganisationRecord organisation, String name, String byLine, OffsetDateTime startTime, OffsetDateTime endTime) {
        DSL.using(databaseService.getConfiguration()).transaction(configuration -> {
            EventRecord event = eventsRepository.createEvent(configuration, organisation, name, byLine, startTime, endTime);

            ParameterMap eventParameters = parameterStoreRepository.getEventParameters(configuration, organisation.getId(), event.getId());
        });
    }
}
