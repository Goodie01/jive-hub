package nz.jive.hub.database;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordContext;
import org.jooq.RecordListener;
import org.jooq.RecordQualifier;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultRecordListenerProvider;
import org.jooq.impl.TableRecordImpl;
import org.jooq.impl.UpdatableRecordImpl;
import nz.jive.hub.JiveConfiguration;
import nz.jive.hub.Main;

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
            .set(new DefaultRecordListenerProvider(new InsertListener()));

    }

    public Configuration getConfiguration() {
        return configuration;
    }


    private static class InsertListener implements RecordListener {
        private static final Field<OffsetDateTime> CREATED_DATE_FIELD = DSL.field(DSL.name("created_date"), OffsetDateTime.class);
        private static final Field<OffsetDateTime> UPDATED_DATE_FIELD = DSL.field(DSL.name("last_updated_date"), OffsetDateTime.class);

        @Override
        public void insertStart(RecordContext ctx) {
            Record record = ctx.record();
            if (record.field(CREATED_DATE_FIELD) != null) {
                record.set(CREATED_DATE_FIELD, OffsetDateTime.now());
            }

            if (record instanceof TableRecordImpl<?> tableRecord) {
                System.out.println("New Record => " + tableRecord.getTable().getName() + " (" + record.get("id") + ")");
            } else {
                System.out.println("Weird new record => " + record.get("id"));
            }

            for (Field<?> field : record.fields()) {
                System.out.println("field: \"" + field.getName() + "\" value: \"" + field.getValue(record) + "\"");
            }
        }

        @Override
        public void updateStart(RecordContext ctx) {
            Record record = ctx.record();
            if (record.field(UPDATED_DATE_FIELD) != null) {
                record.set(UPDATED_DATE_FIELD, OffsetDateTime.now());
            }

            if(record.changed()) {
                if (record instanceof TableRecordImpl<?> tableRecord) {
                    System.out.println("Record change detected => " + tableRecord.getTable().getName() + " (" + record.get("id") + ")");
                } else {
                    System.out.println("Weird record change detected => " + record.get("id"));
                }

                for (Field<?> field : record.fields()) {
                    if(field.changed(record)) {
                        System.out.println("field: \"" + field.getName() + "\" value: \"" + field.original(record) + "\" => \"" + field.getValue(record) + "\"");
                    }
                }
            }
        }
    }
}
