package ddd.application;

public class IssueStateMachineResource {

    /* 
     * POST /issues/23/assignTo 
     * 
     *  Req:
     *  { 
     *      assignee: "bart.simpson@acme.com"
     *  } 
     *  
     *  Resp: 200 OK
     */

    /* 
     * POST /issues/23/markAsDuplicate 
     * 
     *  Req:
     *  { 
     *      duplicatedIssue: 19
     *  } 
     *  
     *  Resp: 200 OK
     */

    /* 
     * POST /issues/23/wontFix 
     * 
     *  Req:
     *  { 
     *      reason: "Out of scope. Not a bug."
     *  } 
     *  
     *  Resp: 200 OK
     */
    
    /* 
     * POST /issues/23/cannotReproduce 
     * 
     *  Resp: 200 OK
     */
    
    /* 
     * POST /issues/23/close 
     * 
     *  Resp: 200 OK
     */
    
    /* 
     * POST /issues/23/reopen 
     * 
     *  Resp: 200 OK
     *  {
     *      version: { product: "super-app", version: "2.0.0.GA" }
     *  }
     */

}
