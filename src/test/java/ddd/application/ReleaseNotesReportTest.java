package ddd.application;

import com.jayway.restassured.http.ContentType;
import ddd.domain.Issue;
import ddd.domain.IssueNumber;
import ddd.domain.ProductID;
import ddd.domain.ProductVersion;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
public class ReleaseNotesReportTest extends IssueResourceBaseTest {

    @Test
    public void shouldGenerateReportForGivenVersionRange() {

        // given:
        // 1.0:
        //   - 881: Fix bug in repository (CLOSED)
        // 1.1:
        //   - 882: Improve performance (OPEN)
        //   - 883: Refactoring of something (CLOSED)
        // 1.2:
        //   - 884: Fix bug somewhere else (CLOSED)
        // 1.3:
        //   - 885: Implement some feature (CLOSED)
        storeIssue(closedIssue(881, "1.0.0", "Fix bug in repository"));
        storeIssue(openedIssue(882, "1.1.0", "Improve performance"));
        storeIssue(closedIssue(883, "1.1.0", "Refactoring of something"));
        storeIssue(closedIssue(884, "1.2.0", "Fix bug somewhere else"));
        storeIssue(closedIssue(885, "1.3.0", "Implement some feature"));

        // when:
        // generate reporort for versions (1.1 -> 1.2)
        given()
                .contentType(ContentType.JSON)
                .get("/app/issues/releaseNotes?from=1.1.0&to=1.2.5").
                then()
                .statusCode(200)
                .body("number", Matchers.contains("883", "884"));
//                .body(Matchers.hasItems(
//                        containsIssue("883", "1.1.0", "Refactoring of something")),
//                        containsIssue("884", "1.2.0", "Fix bug somewhere else"));


        /*

            [
                {version: 1.1, title: '123213, number: 1233}
            ]



         */

        // then:
        // expect report:
        // 1.1:
        //   - 883: Refactoring of something (CLOSED)
        // 1.2:
        //   - 884: Fix bug somewhere else (CLOSED)

    }

    private Matcher<? extends Map> containsIssue(String number, String version, String title) {
        return Matchers.allOf(
            hasEntry("number", number),
                hasEntry("version", version),
                hasEntry("title", title)
        );
    }

    private Issue closedIssue(int id, String version, String title) {
        Issue issue = openedIssue(id, version, title);
        issue.fixedIn(issue.occuredIn());
        issue.close();
        return issue;
    }

    private Issue openedIssue(int id, String version, String title) {
        ProductVersion productVersion = new ProductVersion(new ProductID("Demo"), version);
        return new Issue(new IssueNumber(id), title, productVersion, new Date());
    }


}
