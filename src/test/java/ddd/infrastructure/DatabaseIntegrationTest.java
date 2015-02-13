package ddd.infrastructure;

import java.util.Properties;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.After;
import org.junit.Before;

public class DatabaseIntegrationTest {

    private EJBContainer container;
    private Context context;
    
    private EntityManagerHolder entityManager;
    
    @Before
    public void startTestContainer() throws NamingException {
        
        final Properties p = new Properties();
        p.put("issuesDatabase", "new://Resource?type=DataSource");
        p.put("issuesDatabase.JdbcDriver", "org.hsqldb.jdbcDriver");
        p.put("issuesDatabase.JdbcUrl", "jdbc:hsqldb:mem:issuesdb");
        p.put("issuesDatabase.LogSql", "true");
    
        container = EJBContainer.createEJBContainer(p);
        context = container.getContext();
        
        entityManager = lookup(EntityManagerHolder.class);
    }

    @Stateless
    public static class EntityManagerHolder {
        
        @PersistenceContext(unitName="issues-unit")
        private EntityManager entityManager;

        public EntityManager entityManager() {
            return entityManager;
        }
        
        @TransactionAttribute(TransactionAttributeType.REQUIRED)
        public void flushAndClear() {
            entityManager.flush();
            entityManager.clear();
        }
    }
    
    @After
    public void shutdownTestContainer() {
        container.close();
    }
    
    @SuppressWarnings("unchecked")
    protected <T> T lookup(Class<T> clazz) throws NamingException {
        return (T) context.lookup("java:global/issues-ddd/" + clazz.getSimpleName());
    }

    protected void flushAndClear() {
        entityManager.flushAndClear();
    }

}