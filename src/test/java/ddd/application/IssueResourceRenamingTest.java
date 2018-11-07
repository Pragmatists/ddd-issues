package ddd.application;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Date;

import javax.persistence.EntityManager;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.http.ContentType;

import ddd.domain.Issue;
import ddd.domain.IssueNumber;
import ddd.domain.IssueRepository;
import ddd.domain.ProductVersion;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class IssueResourceRenamingTest extends IssueResourceBaseTest {

    @Autowired
    private IssueRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void shouldGetIssuesByIssueNumber() {
        
        // given:
        doInTransaction(() -> {
            
            repository.store(anIssue(new IssueNumber(345), "Old Issue Title"));
            
        });
        
        IssueResource.RenameIssueRequestJson request = new IssueResource.RenameIssueRequestJson("New Issue Title");
        
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


    @Test
    public void shouldReturnNotFoundWhenIssueNotExistForRename() {

        IssueResource.RenameIssueRequestJson request = new IssueResource.RenameIssueRequestJson("New Issue Title");

        // when:
        given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/app/issues/345/rename").
                then()
                .statusCode(404)
                .body("errorMsg", equalTo("Issue with number='345' does not exist!"));

    }




    // --
    
    private Issue anIssue(IssueNumber number, String title) {
        return new Issue(number, title, ProductVersion.of("buggy-app 1.0.0"), aDate());
    }

    private Date aDate() {
        return new Date();
    }
}
