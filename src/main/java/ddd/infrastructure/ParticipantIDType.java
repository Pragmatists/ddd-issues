package ddd.infrastructure;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.UserType;

import ddd.domain.ParticipantID;

public class ParticipantIDType extends ValueObjectType implements UserType {

    public ParticipantIDType() {
        super(ParticipantID.class);
    }

    @Override
    public int[] sqlTypes() {
        return new int[]{StandardBasicTypes.STRING.sqlType()};
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session,
                              Object owner) throws HibernateException, SQLException {
        
        String value = rs.getString(names[0]);
        return value == null ? null : new ParticipantID(value);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index,
                            SessionImplementor session) throws HibernateException, SQLException {
        
        String representation = value != null ? value.toString() : null;
        if (null != representation) {
            st.setString(index, representation);
        } else {
            st.setNull(index, StandardBasicTypes.STRING.sqlType());
        }
    }

}
