package ddd.application;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import ddd.domain.Issue;
import ddd.domain.IssueFactory;
import ddd.domain.IssueNumber;
import ddd.domain.IssueRepository;
import ddd.domain.Issues;
import ddd.domain.ProductID;
import ddd.domain.ProductVersion;

@Stateless
@Path("/issues")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class IssueResource {

    @Inject
    private IssueFactory factory;

    @Inject
    private IssueRepository repository;

    @Inject
    private Issues issues;

    @POST
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Response create(NewIssueJson issueJson) throws URISyntaxException {
        
        ProductVersion version = new ProductVersion(new ProductID(issueJson.occurredIn.product), issueJson.occurredIn.version);
        
        Issue issue = factory.newBug(issueJson.title, issueJson.description, version);
        repository.store(issue);
        
        return Response.created(new URI(String.format("/issues/%s", issue.number()))).build();
    }

    @GET
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Response list() {
        
        IssuesJson issuesJson = new IssuesJson();
        issues.forEach(issue -> issuesJson.add(new ExistingIssueJson(issue)));
        
        return Response.ok(issuesJson).build();
    }

    @GET
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Path("/{issueNumber}")
    public Response get(@PathParam("issueNumber") Integer issueNumber) throws URISyntaxException {
        
        Issue load = repository.load(new IssueNumber(issueNumber));
        return Response.ok(new ExistingIssueJson(load)).build();
    }
    
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    static class NewIssueJson {

        String title = "";
        String description = "";
        OccurredInJson occurredIn = new OccurredInJson();

        public NewIssueJson() {}

        public NewIssueJson(Issue issue) {
            title = issue.title();
            description = issue.description();
            occurredIn = new OccurredInJson(issue.occuredIn());
        }
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    static class ExistingIssueJson {

        String number;
        String title;
        String description;
        OccurredInJson occurredIn = new OccurredInJson();

        public ExistingIssueJson() {}

        public ExistingIssueJson(Issue issue) {
            number = issue.number().toString();
            title = issue.title();
            description = issue.description();
            occurredIn = new OccurredInJson(issue.occuredIn());
        }
    }
    
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    static class OccurredInJson {
        
        String product;
        String version;

        public OccurredInJson() {}

        public OccurredInJson(ProductVersion occuredIn) {
            product = occuredIn.product().toString();
            version = occuredIn.toString();
        }
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class IssuesJson extends ArrayList<ExistingIssueJson> {
    }

}
