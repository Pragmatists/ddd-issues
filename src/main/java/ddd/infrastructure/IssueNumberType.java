package ddd.infrastructure;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.UserType;

import ddd.domain.IssueNumber;

public class IssueNumberType extends ValueObjectType implements UserType {

    public IssueNumberType() {
        super(IssueNumber.class);
    }

    @Override
    public int[] sqlTypes() {
        return new int[]{StandardBasicTypes.INTEGER.sqlType()};
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] names, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException, SQLException {
        int value = resultSet.getInt(names[0]);
        return value == 0 ? null : new IssueNumber(value);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {
        String representation = value != null ? value.toString() : null;
        if (null != representation) {
            st.setInt(index, Integer.parseInt(representation));
        } else {
            st.setNull(index, StandardBasicTypes.INTEGER.sqlType());
        }
    }

}
