package ddd.application;

import static com.jayway.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

import javax.inject.Inject;

import org.junit.Test;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import ddd.domain.Greetings;

public class IssuesResourceTest extends EndToEndTest {

    @Inject
    private Greetings greetings;
    
    @Inject
    private HelloResource helloResource;

    @Test
    public void shouldCreateResource() throws Exception {

        Response response = given().contentType(ContentType.JSON).body(new IssueResource.IssueJson())

                .post("/app/issues").thenReturn();

        assertThat(response.getStatusCode()).isEqualTo(201);
        assertThat(response.getHeader("Location")).matches("/issues/\\d+");
    }
}
