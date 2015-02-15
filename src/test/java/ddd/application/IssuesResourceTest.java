package ddd.application;

import ddd.infrastructure.DatabaseIntegrationTest;
import ddd.infrastructure.JpaFooRepository;
import org.junit.Before;
import org.junit.Test;

import javax.naming.NamingException;

public class IssuesResourceTest extends DatabaseIntegrationTest {

    JpaFooRepository repository;

    IssueResource resource;


    @Before
    public void setUp() throws NamingException {
        resource = lookup(IssueResource.class);
        repository = lookup(JpaFooRepository.class);
    }

    @Test
    public void shouldCreateIssue() throws Exception {
        IssueResource.IssueJson request = new IssueResource.IssueJson();
        request.title = "Title";

        resource.create(request);

//        repository.load();
    }
}
