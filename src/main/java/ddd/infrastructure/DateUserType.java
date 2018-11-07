package ddd.infrastructure;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

public class DateUserType implements UserType {

    @Override
    public int[] sqlTypes() {
         return new int[]{Types.TIMESTAMP};
    }
 
    @SuppressWarnings("rawtypes")
    @Override
    public Class returnedClass() {
        return Date.class;
    }
 
    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
         return x == y || !(x == null || y == null) && x.equals(y);
    }
 
    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }
 
    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        Timestamp timestamp = rs.getTimestamp(names[0]);
        if (rs.wasNull()) {
            return null;
        }
        return new Date(timestamp.getTime());
    }
 
    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.TIMESTAMP);
        }
        else {
            Date date = (Date) value;
            Timestamp timestamp = new Timestamp(date.getTime());
            st.setTimestamp(index, timestamp);
        }
    }
 
    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }
 
    @Override
    public boolean isMutable() {
        return false;
    }
 
    @Override
    public Serializable disassemble(Object value) throws HibernateException {
         return (Serializable) value;
    }
 
    @Override
    public Object assemble(Serializable cached, Object owner)
            throws HibernateException {
        return cached;
    }
 
    @Override
    public Object replace(Object original, Object target, Object owner)
            throws HibernateException {
        return original;
    }

}
