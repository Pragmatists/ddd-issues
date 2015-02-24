package ddd.application;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import ddd.domain.Issue;
import ddd.domain.IssueFactory;
import ddd.domain.IssueNumber;
import ddd.domain.IssueRepository;
import ddd.domain.ProductID;
import ddd.domain.ProductVersion;

public class IssuesResourceTest extends EndToEndTest {

    @Inject
    private IssueRepository repository;

    @Inject
    private IssueFactory factory;

    @PersistenceContext(unitName="issues-unit")
    private EntityManager entityManager;

    @Test
    public void shouldCreateResource() throws Exception {

        // given:
        IssueResource.NewIssueJson request = new IssueResource.NewIssueJson();
        request.title = "First Issue";
        request.description = "Description";
        request.occurredIn.product = "supper-app";
        request.occurredIn.version = "1.2.10";

        // when:
        Response response = given().contentType(ContentType.JSON).body(request)
                .post("/app/issues");

        // then:
        assertThat(response.getStatusCode()).isEqualTo(201);
        assertThat(response.getHeader("Location")).matches(".*/issues/\\d+");
    }

    @Test
    public void shouldPersistResource() throws Exception {

        // given:
        IssueResource.NewIssueJson request = new IssueResource.NewIssueJson();
        request.title = "First Issue";
        request.description = "Description";
        request.occurredIn.product = "supper-app";
        request.occurredIn.version = "1.2.10";

        // when:
        Response response = given().contentType(ContentType.JSON).body(request)
                .post("/app/issues").thenReturn();

        // then:
        IssueNumber id = extractFrom(response.getHeader("Location"));
        Issue load = repository.load(id);
        assertThat(load.title()).isEqualTo("First Issue");
        assertThat(load.description()).isEqualTo("Description");
        assertThat(load.createdAt()).isNotNull();
        assertThat(load.occuredIn()).isEqualTo(new ProductVersion(new ProductID("supper-app"), "1.2.10"));

    }

    @Test
    public void shouldListIssues() {

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

    // --
    
    private Issue anIssue(String title) {
        return factory.newBug(title, "Description of issue #1", new ProductVersion(new ProductID("acme"), "1.4.81"));
    }

    private IssueNumber extractFrom(String location) {
        Pattern pattern = Pattern.compile(".*/app/issues/(\\d+)");
        Matcher matcher = pattern.matcher(location);
        if (matcher.matches()) {
            return new IssueNumber(Integer.valueOf(matcher.group(1)));
        }
        throw new IllegalStateException(String.format("Missing IssueNumber in '%s'", location));
    }
}
