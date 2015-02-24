package ddd.infrastructure;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.TimestampType;

public class DateType extends TimestampType {

    @Override
    public Object get(ResultSet rs, String name, SessionImplementor session) throws HibernateException, SQLException {
        Timestamp ts = rs.getTimestamp(name);

        Date result = null;
        if(ts != null){
            result = new Date(ts.getTime());
        }

        return result;
    }
    
}
