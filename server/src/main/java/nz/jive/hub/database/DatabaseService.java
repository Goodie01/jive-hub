package nz.jive.hub.database;

import nz.jive.hub.JiveConfiguration;
import nz.jive.hub.perf.TimingResults;
import org.jetbrains.annotations.NotNull;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.tools.StopWatch;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.OffsetDateTime;

/**
 * @author thomas.goodwin
 */
public class DatabaseService {
    private final Configuration configuration;

    public DatabaseService() throws SQLException {
        System.setProperty("org.jooq.no-logo", "true");
        System.setProperty("org.jooq.no-tips", "true");

        var connection = DriverManager.getConnection(JiveConfiguration.DATABASE_JDBC.valueOf());
        configuration = new DefaultConfiguration()
                .set(connection)
                .set(SQLDialect.POSTGRES)
                .set(new ExecuteListenerProvider() {
                    @NotNull
                    @Override
                    public ExecuteListener provide() {
                        return new Logger();
                    }
                })
                .set(new InsertListener());

    }

    public Configuration getConfiguration() {
        return configuration;
    }

    private static class Logger implements ExecuteListener {
        StopWatch stopWatch;

        @Override
        public void executeStart(ExecuteContext ctx) {
            stopWatch = new StopWatch();
        }

        @Override
        public void executeEnd(ExecuteContext ctx) {
            long split = stopWatch.split();
            TimingResults.getInstance().addInvocation(ctx.sql(), split);
        }
    }


    private static class InsertListener implements RecordListener {
        private static final Field<OffsetDateTime> CREATED_DATE_FIELD
                = DSL.field(DSL.name("created_date"), OffsetDateTime.class);
        private static final Field<OffsetDateTime> UPDATED_DATE_FIELD
                = DSL.field(DSL.name("last_updated_date"), OffsetDateTime.class);

        @Override
        public void insertStart(RecordContext ctx) {
            Record record = ctx.record();
            if (record.field(CREATED_DATE_FIELD) != null) {
                record.set(CREATED_DATE_FIELD, OffsetDateTime.now());
            }
        }

        @Override
        public void updateStart(RecordContext ctx) {
            Record record = ctx.record();
            if (record.field(UPDATED_DATE_FIELD) != null) {
                record.set(UPDATED_DATE_FIELD, OffsetDateTime.now());
            }
        }
    }
}
