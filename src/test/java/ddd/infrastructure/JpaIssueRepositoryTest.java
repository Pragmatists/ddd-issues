package ddd.infrastructure;

import static ddd.infrastructure.TestComparators.fieldByField;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;
import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import ddd.domain.Issue;
import ddd.domain.IssueNumber;
import ddd.domain.IssueRepository;
import ddd.domain.ParticipantID;
import ddd.domain.ProductID;
import ddd.domain.ProductVersion;

@RunWith(Arquillian.class)
@Transactional(value=TransactionMode.ROLLBACK)
public class JpaIssueRepositoryTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
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

    @Before
    public void setUp() {

        repository = new JpaIssueRepository(entityManager){
            @Override
            public void store(Issue issue) {
                super.store(issue);
                entityManager.flush();
                entityManager.clear();
            }
        };
    }
    
    @Test
    public void shouldStoreAndLoadIssue() throws Exception {

        // given:
        Issue issue = anIssue(new IssueNumber(123));

        // when:
        repository.store(issue);
        
        // then:
        Issue loaded = repository.load(new IssueNumber(123));
        assertThat(loaded)
            .usingComparator(fieldByField(Issue.class))
            .describedAs("%s vs %s", reflectionToString(loaded, MULTI_LINE_STYLE), reflectionToString(issue, MULTI_LINE_STYLE))
            .isEqualTo(issue);
    }

    @Test
    public void shouldFailMeaningfullyIfIssueWithGivenNumberAlreadyExist() throws Exception {

        // expect:
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Issue with number='123' already exists!");
        
        // given:
        issueAlreadyExists(new IssueNumber(123));

        // when:
        repository.store(anIssue(new IssueNumber(123)));
    }

    @Test
    public void shouldFailMeaningfullyIfIssueWithGivenNumberDoesNotExist() throws Exception {
        
        // expect:
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Issue with number='404' does not exist!");
            
        // when:
        repository.load(new IssueNumber(404));
    }

    // --

    private Issue anIssue(IssueNumber issueNumber) {
        Issue issue = new Issue(issueNumber, "System does not work!", aProductVersion(), aDate());
        issue.assignTo(new ParticipantID("homer.simpson"));
        issue.fixedIn(aProductVersion("4.5.6"));
        issue.updateDescription("This is very very long description of this issue!");
        issue.referTo(new IssueNumber(888));
        issue.blocks(new IssueNumber(777));
        return issue;
    }
    
    private Issue issueAlreadyExists(IssueNumber issueNumber) {
        Issue issue = anIssue(issueNumber);
        repository.store(issue);
        return issue;
    }
    
    private Date aDate() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-10-11 12:34:56");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private ProductVersion aProductVersion() {
        return aProductVersion("1.2.3");
    }

    private ProductVersion aProductVersion(String version) {
        return new ProductVersion(new ProductID("buggy-app"), version);
    }
}