package ddd.application;

import java.net.URI;
import java.net.URISyntaxException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import ddd.domain.Issue;
import ddd.domain.IssueFactory;
import ddd.domain.IssueRepository;
import ddd.domain.ProductVersion;

@Stateless
@Path("/issues")
public class IssueResource {

    @Inject
    private IssueFactory factory;
    @Inject
    private IssueRepository repository;
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

    public void create(IssueJson issueJson) throws URISyntaxException {
        Issue issue = factory.newBug(issueJson.title, issueJson.descripton, new ProductVersion());
        repository.store(issue);
        Response.created(new URI(String.format("/issue/%s", issue.number())));
    }

    static class IssueJson{
        String title;
        String descripton;

        OccurrenceJson occurrenceIn;
    }

    static class OccurrenceJson {
        String product;
        String version;
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
