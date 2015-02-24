package ddd.infrastructure;

import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.jdbc.meta.ValueHandler;
import org.apache.openjpa.jdbc.meta.ValueMapping;

import ddd.domain.ProductVersion;

public class ProductVersionSerializationStrategy extends ValueObjectSerializationStrategy implements ValueHandler {

    @Override
    public Object toDataStoreValue(ValueMapping vm, Object val, JDBCStore store) {
        return val.toString();
    }

    @Override
    public Object toObjectValue(ValueMapping vm, Object val) {
        return ProductVersion.of(val.toString());
    }

}
