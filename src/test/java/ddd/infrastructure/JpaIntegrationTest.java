package ddd.infrastructure;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import ddd.domain.IssueNumber;
import ddd.domain.IssueRepository;

@RunWith(Arquillian.class)
@Transactional(value=TransactionMode.ROLLBACK)
public class JpaIntegrationTest {

    @PersistenceContext
    private EntityManager entityManager;

    private IssueRepository repository;
    
    @Deployment
    public static JavaArchive deployment(){
        return ShrinkWrap.create(JavaArchive.class)
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsResource("log4j.properties")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void should() throws Exception {
        
        repository.load(new IssueNumber(213)); // ???
    }

}
