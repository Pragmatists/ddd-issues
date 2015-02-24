package ddd.infrastructure;

import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.jdbc.meta.ValueHandler;
import org.apache.openjpa.jdbc.meta.ValueMapping;

import ddd.domain.ProductVersion;

public class ProductVersionSerializationStrategy extends ValueObjectSerializationStrategy implements ValueHandler {

    @Override
    public Object toDataStoreValue(ValueMapping vm, Object val, JDBCStore store) {
        return val == null ? null : val.toString();
    }

    @Override
    public Object toObjectValue(ValueMapping vm, Object val) {
        return val == null ? null : ProductVersion.of(val.toString());
    }

}
