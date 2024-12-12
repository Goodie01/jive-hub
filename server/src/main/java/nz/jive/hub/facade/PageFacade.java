package nz.jive.hub.facade;

import nz.jive.hub.Repository.PageRepository;
import nz.jive.hub.database.DatabaseService;
import nz.jive.hub.database.generated.tables.records.PageRecord;

import java.util.Optional;

/**
 * @author Goodie
 */
public class PageFacade {
    private final DatabaseService databaseService;
    private final PageRepository pageRepository;

    public PageFacade(DatabaseService databaseService, PageRepository pageRepository) {
        this.databaseService = databaseService;
        this.pageRepository = pageRepository;
    }

    public Optional<PageRecord> find(final int organisationId, final String path) {
        return pageRepository.find(databaseService.getConfiguration(), organisationId, path);
    }
}
