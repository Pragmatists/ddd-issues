package ddd.application;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import ddd.application.IssueResource.ExistingIssueJson;
import ddd.domain.Issue;
import ddd.domain.IssueFactory;
import ddd.domain.IssueNumber;
import ddd.domain.IssueRepository;
import ddd.domain.ParticipantID;
import ddd.domain.ProductID;
import ddd.domain.ProductVersion;

public class IssuesResourceListing extends EndToEndTest {

    @Inject
    private IssueRepository repository;

    @Inject
    private IssueFactory factory;

    @PersistenceContext(unitName="issues-unit")
    private EntityManager entityManager;

    @Before
    public void setUp() {

        doInTransaction(() -> {
            entityManager.createQuery("delete from Issue").executeUpdate();
        });
    }
    
    @Test
    public void shouldListAllIssues() {

        // given:
        doInTransaction(() -> {
            repository.store(anIssue("Issue #1"));
            repository.store(anIssue("Issue #2"));
        });

        // when:
        Response response = given().contentType(ContentType.JSON)
                .get("/app/issues").thenReturn();

        // then:
        assertThat(response.getStatusCode()).isEqualTo(200);
        IssueResource.IssuesJson issues = response.as(IssueResource.IssuesJson.class);
        assertThat(issues).hasSize(2);
    }

    @Test
    public void shouldGetIssuesByIssueNumber() {
        
        // given:
        doInTransaction(() -> {
            
            Issue issue = new Issue(new IssueNumber(345), "Issue Title", ProductVersion.of("buggy-app 1.0.0"), aDate("2014-10-11 20:12:00"));
            issue.assignTo(new ParticipantID("homer.simpson"));
            issue.fixedIn(ProductVersion.of("buggy-app 1.0.2"));
            repository.store(issue);
            
        });
        
        // when:
        Response response = given().contentType(ContentType.JSON)
                .get("/app/issues/345").thenReturn();
        
        // then:
        assertThat(response.getStatusCode()).isEqualTo(200);
        ExistingIssueJson issue = response.as(ExistingIssueJson.class);
        assertThat(issue.number).isEqualTo("345");
        assertThat(issue.title).isEqualTo("Issue Title");
        assertThat(issue.status).isEqualTo("resolved");
        assertThat(issue.resolution).isEqualTo("fixed");
        assertThat(issue.occurredIn.product).isEqualTo("buggy-app");
        assertThat(issue.occurredIn.version).isEqualTo("1.0.0");
        assertThat(issue.fixedIn.product).isEqualTo("buggy-app");
        assertThat(issue.fixedIn.version).isEqualTo("1.0.2");
        assertThat(issue.createdAt).isEqualTo("2014-10-11 20:12:00");
        assertThat(issue.assignee).isEqualTo("homer.simpson");
    }
    
    // --
    
    private Date aDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Issue anIssue(String title) {
        return factory.newBug(title, "Description of issue #1", new ProductVersion(new ProductID("buggy-app"), "1.4.81"));
    }

}
