package ddd.application;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    public Response create(NewIssueJson json) throws URISyntaxException {
        
        ProductVersion version = new ProductVersion(new ProductID(json.occurredIn.product), json.occurredIn.version);
        
        Issue issue = factory.newBug(json.title, json.description, version);
        repository.store(issue);
        
        return Response.created(new URI(String.format("/issues/%s", issue.number()))).build();
    }

    @GET
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Response list() {
        
        IssuesJson json = new IssuesJson();
        issues.forEach(issue -> json.add(new ExistingIssueJson(issue)));
        
        return Response.ok(json).build();
    }

    @GET
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Path("/{issueNumber}")
    public Response get(@PathParam("issueNumber") Integer issueNumber) throws URISyntaxException {
        
        Issue load = repository.load(new IssueNumber(issueNumber));
        return Response.ok(new ExistingIssueJson(load)).build();
    }

    @POST
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Path("/{issueNumber}/rename")
    public Response rename(@PathParam("issueNumber") Integer issueNumber, RenameIssueJson json) throws URISyntaxException {
        
        Issue issue = repository.load(new IssueNumber(issueNumber));
        issue.renameTo(json.newTitle);
        return Response.ok().build();
    }
    
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    static class NewIssueJson {

        String title = "";
        String description = "";
        VersionJson occurredIn = new VersionJson();

        public NewIssueJson() {}

        public NewIssueJson(Issue issue) {
            title = issue.title();
            description = issue.description();
            occurredIn = new VersionJson(issue.occuredIn());
        }
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    static class RenameIssueJson {
        
        String newTitle = "";
        
        public RenameIssueJson() {}
        
        public RenameIssueJson(String newTitle) {
            this.newTitle = newTitle;
        }
    }
    
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    static class ExistingIssueJson {

        String number;
        String title;
        String description;
        VersionJson occurredIn = new VersionJson();
        VersionJson fixedIn = new VersionJson();
        String status;
        String resolution;
        String createdAt;
        String assignee;

        public ExistingIssueJson() {}

        public ExistingIssueJson(Issue issue) {
            number = issue.number().toString();
            title = issue.title();
            description = issue.description();
            status = issue.status().name().toLowerCase();
            resolution = issue.resolution() == null ? null : issue.resolution().name().toLowerCase();
            createdAt = format(issue.createdAt());
            occurredIn = new VersionJson(issue.occuredIn());
            fixedIn = issue.fixVersion() == null ? null :  new VersionJson(issue.fixVersion());
            assignee = issue.assignee() == null ? null :  issue.assignee().toString();
        }

    }
    
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    static class VersionJson {
        
        String product;
        String version;

        public VersionJson() {}

        public VersionJson(ProductVersion productVersion) {
            product = productVersion.toString().split(" ")[0];
            version = productVersion.toString().split(" ")[1];
        }
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class IssuesJson extends ArrayList<ExistingIssueJson> {
    }

    private static String format(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }
}
