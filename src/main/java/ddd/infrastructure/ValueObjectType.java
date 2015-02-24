package ddd.infrastructure;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

public abstract class ValueObjectType implements UserType {

    private Class<?> valueObjectClass;

    public ValueObjectType(Class<?> valueObjectClass) {
        this.valueObjectClass = valueObjectClass;
    }

    @Override
    public Class<?> returnedClass() {
        return valueObjectClass;
    }
    
    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if (null == x || null == y) {
            return false;
        }
        return x.equals(y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public Object deepCopy(Object obj) throws HibernateException {
        return obj;
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
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

}