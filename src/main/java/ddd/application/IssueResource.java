package ddd.application;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import ddd.domain.Issue;
import ddd.domain.IssueFactory;
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
    /* 
     * POST /issues 
     * 
     *  Req:
     *  { 
     *      title: "Issue Title",
     *      description: "Title Description", 
     *      occuredIn: { product: "supper-app", version: "1.2.10" } 
     *  } 
     *  
     *  Resp: 201 Created, Location: /issues/23
     */

    @POST
    @Transactional
    public Response create(IssueJson issueJson) throws URISyntaxException {
        Issue issue = factory.newBug(issueJson.title, issueJson.description, new ProductVersion(new ProductID(issueJson.occurredIn
                .product), issueJson.occurredIn.version));
        repository.store(issue);
        return Response.created(new URI(String.format("/issues/%s", issue.number()))).build();
    }
     /*
     * GET /issues
     *
     *  Resp: 200 OK
     *  [{
     *      number: 23,
     *      title: "Issue Title",
     *      description: "Title Description",
     *      createdAt: 12323453423,
     *      reportedBy: "homer.simpson@acme.com",
     *      occuredIn: { product: "supper-app", version: "1.2.10" }
     *      fixedIn: undefined,
     *      status: "OPEN",
     *      resolution: undefined
     *      relatedIssues: []
     *  }]
     */
     @GET
     @Transactional
     public IssuesJson list() throws URISyntaxException {
         IssuesJson issuesJson = new IssuesJson();

         for (Issue issue : issues) {
             issuesJson.add(new IssueJson());
         }
         return issuesJson;
     }

    @XmlRootElement(name = "IssueJson")
    @XmlAccessorType(XmlAccessType.FIELD)
    static class IssueJson {
        String title = "";

        String description = "";

        OccurredInJson occurredIn = new OccurredInJson();
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    static class OccurredInJson {
        String product;

        String version;
    }

    public static class IssuesJson extends ArrayList<IssueJson> {

    }
    

    
    /* 
     * GET /issues/23 
     *  
     *  Resp: 200 OK
     *  { 
     *      number: 23,
     *      title: "Issue Title",
     *      description: "Title Description", 
     *      createdAt: 12323453423,
     *      reportedBy: "homer.simpson@acme.com",
     *      assignedTo: undefined,
     *      occuredIn: { product: "supper-app", version: "1.2.10" }
     *      fixedIn: undefined,
     *      status: "OPEN",
     *      resolution: undefined 
     *      relatedIssues: []
     *  } 
     */

}
