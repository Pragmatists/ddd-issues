package ddd.infrastructure;

import static ddd.infrastructure.TestComparators.fieldByField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import java.util.Date;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import ddd.domain.Issue;
import ddd.domain.IssueNumber;
import ddd.domain.IssueRepository;
import ddd.domain.ProductID;
import ddd.domain.ProductVersion;

@RunWith(Arquillian.class)
public class JpaIssueRepositoryTest {

    @Inject
    private IssueRepository repository;
    
    @Deployment
    public static JavaArchive deployment(){
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(JpaIssueRepository.class)
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    
    @Test
    @Transactional(value=TransactionMode.ROLLBACK)
    public void shouldStoreAndLoadIssue() throws Exception {

        // given:
        Issue issue = anIssue(new IssueNumber(123));
        
        // when:
        repository.store(issue);

        Issue loaded = repository.load(new IssueNumber(123));
        
        // then:
        assertThat(loaded)
            .usingComparator(fieldByField())
            .isEqualTo(issue);
    }

    @Test
    @Transactional(value=TransactionMode.ROLLBACK)
    public void shouldFailMeaningfullyIfIssueWithGivenNumberAlreadyExist() throws Exception {

        // given:
        Issue issue = anIssue(new IssueNumber(123));
        
        repository.store(issue);

        try {
            
            // when:
            repository.store(issue);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
            
        } catch(Exception e){
            
            // then:
            assertThat(e)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Issue with number='123' already exists!");
        }
        
    }


    // --
    
    private Issue anIssue(IssueNumber issueNumber) {
        return new Issue(issueNumber, "System does not work!", aProductVersion(), aDate());
    }
    
    private Date aDate() {
        return new Date();
    }

    private ProductVersion aProductVersion() {
        return new ProductVersion(new ProductID("buggy"), "1.2.3");
    }
}
