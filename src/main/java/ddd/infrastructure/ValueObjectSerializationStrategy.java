package ddd.infrastructure;

import java.sql.SQLException;

import org.apache.openjpa.jdbc.identifier.DBIdentifier;
import org.apache.openjpa.jdbc.kernel.JDBCFetchConfiguration;
import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.jdbc.meta.ValueHandler;
import org.apache.openjpa.jdbc.meta.ValueMapping;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.schema.ColumnIO;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.meta.JavaTypes;

public abstract class ValueObjectSerializationStrategy implements ValueHandler{

    @Override
    public Column[] map(ValueMapping vm, String name, ColumnIO io, boolean adapt) {
        Column column = new Column();
        column.setIdentifier(DBIdentifier.newColumn(name));
        column.setJavaType(JavaTypes.STRING);
        return new Column[]{column};
    }

    @Override
    public boolean isVersionable(ValueMapping vm) {
        return false;
    }

    @Override
    public boolean objectValueRequiresLoad(ValueMapping vm) {
        return false;
    }

    @Override
    public Object getResultArgument(ValueMapping vm) {
        return null;
    }

    @Override
    public Object toObjectValue(ValueMapping vm, Object val, OpenJPAStateManager sm, JDBCStore store, JDBCFetchConfiguration fetch) throws SQLException {
        return null;
    }

}