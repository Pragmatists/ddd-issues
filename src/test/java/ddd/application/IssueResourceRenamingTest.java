package ddd.application;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;

import com.jayway.restassured.http.ContentType;

import ddd.domain.Issue;
import ddd.domain.IssueNumber;
import ddd.domain.IssueRepository;
import ddd.domain.ProductVersion;

public class IssueResourceRenamingTest extends EndToEndTest {

    @Inject
    private IssueRepository repository;

    @PersistenceContext(unitName="issues-unit")
    private EntityManager entityManager;

    @Test
    public void shouldGetIssuesByIssueNumber() {
        
        // given:
        doInTransaction(() -> {
            
            repository.store(anIssue(new IssueNumber(345), "Old Issue Title"));
            
        });
        
        IssueResource.RenameIssueJson request = new IssueResource.RenameIssueJson("New Issue Title");
        
        // when:
        given()
            .contentType(ContentType.JSON)
            .body(request)
            .post("/app/issues/345/rename").
        then()
            .statusCode(200);
        
        // then:
        Issue load = repository.load(new IssueNumber(345));
        assertThat(load.title()).isEqualTo("New Issue Title");
    }
    
    // --
    
    private Issue anIssue(IssueNumber number, String title) {
        return new Issue(number, title, ProductVersion.of("buggy-app 1.0.0"), aDate());
    }

    private Date aDate() {
        return new Date();
    }
}
