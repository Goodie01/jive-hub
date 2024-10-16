package nz.jive.hub.database;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import nz.jive.hub.JiveConfiguration;
import org.jooq.Configuration;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordContext;
import org.jooq.RecordListener;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

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
            .set(new InsertListener())
            .set(new Logger());

    }

    public Configuration getConfiguration() {
        return configuration;
    }

    private static class Logger implements ExecuteListener {
        @Override
        public void executeEnd(ExecuteContext ctx) {
            System.out.println(ctx.sql());
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

//            if (record instanceof TableRecordImpl<?> tableRecord) {
//                System.out.println("New Record => " + tableRecord
//                    .getTable()
//                    .getName() + " (" + record.get("id") + ")");
//            } else {
//                System.out.println("Weird new record => " + record.get("id"));
//            }
//
//            for (Field<?> field : record.fields()) {
//                System.out.println("field: \"" + field.getName() + "\" value: \"" + field.getValue(record) + "\"");
//            }
        }

        @Override
        public void updateStart(RecordContext ctx) {
            Record record = ctx.record();
            if (record.field(UPDATED_DATE_FIELD) != null) {
                record.set(UPDATED_DATE_FIELD, OffsetDateTime.now());
            }

//            if (record.changed()) {
//                if (record instanceof TableRecordImpl<?> tableRecord) {
//                    System.out.println("Record change detected => " + tableRecord
//                        .getTable()
//                        .getName() + " (" + record.get("id") + ")");
//                } else {
//                    System.out.println("Weird record change detected => " + record.get("id"));
//                }
//
//                for (Field<?> field : record.fields()) {
//                    if (field.changed(record)) {
//                        System.out.println("field: \"" + field.getName()
//                            + "\" value: \"" + field.original(record)
//                            + "\" => \"" + field.getValue(record) + "\"");
//                    }
//                }
//            }
        }
    }
}
