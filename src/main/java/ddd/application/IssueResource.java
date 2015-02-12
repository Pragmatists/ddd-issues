package ddd.application;

public class IssueResource {

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
