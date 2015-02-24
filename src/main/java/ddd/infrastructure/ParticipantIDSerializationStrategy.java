package ddd.infrastructure;

import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.jdbc.meta.ValueMapping;

import ddd.domain.ParticipantID;

public class ParticipantIDSerializationStrategy extends ValueObjectSerializationStrategy {

    @Override
    public Object toDataStoreValue(ValueMapping vm, Object val, JDBCStore store) {
        return val == null ? null : val.toString();
    }

    @Override
    public Object toObjectValue(ValueMapping vm, Object val) {
        return val == null ? null : new ParticipantID(val.toString());
    }

}
