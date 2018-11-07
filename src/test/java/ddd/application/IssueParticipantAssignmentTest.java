package ddd.application;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import ddd.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
public class IssueParticipantAssignmentTest extends IssueResourceBaseTest {

    public static final IssueNumber ISSUE_NUMBER = new IssueNumber(345);

    @Test
    public void shouldAssignExsitingParticipant() {

        // given:
        storeIssue(anIssue(ISSUE_NUMBER));

        // when:
        postAssigneeParticipant("4444").
                then()
                .statusCode(200);

        // then:
        Issue load = repository.load(ISSUE_NUMBER);
        assertThat(load.assignee()).isEqualTo(new ParticipantID("4444"));
    }

    @Test
    public void shouldReturnNotFoundWhenIssueNotExistForAssignParticipant() {

        // when:
        postAssigneeParticipant("4444").
                then()
                .statusCode(404)
                .body("errorMsg", equalTo("Issue with number='345' does not exist!"));
    }

    @Test
    public void shouldReturn400IfIssueIsAlreadyClosedWhenAssigningParticipant() {

        // given:
        storeIssue(anAlreadyClosedIssue(ISSUE_NUMBER));

        // when:
        postAssigneeParticipant("4444").
                then().statusCode(400).body("errorMsg", equalTo("Cannot assign already CLOSED issue!"));
    }

    // --

    private Response postAssigneeParticipant(String participantId) {
        return given()
                .contentType(ContentType.JSON)
                .body(new IssueResource.AssignExistingPaticipantRequestJson(participantId))
                .post("/app/issues/{issueNumber}/assignParticipant", ISSUE_NUMBER);
    }

    private Issue anIssue(IssueNumber number) {
        return new Issue(number, "Sample title", ProductVersion.of("buggy-app 1.0.0"), aDate());
    }

    private Issue anAlreadyClosedIssue(IssueNumber number) {
        Issue issue = anIssue(number);
        issue.fixedIn(ProductVersion.of("my-app 1.2.4"));
        issue.close();
        return issue;
    }


    private Date aDate() {
        return new Date();
    }
}
