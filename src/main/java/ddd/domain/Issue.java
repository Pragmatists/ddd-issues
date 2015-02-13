package ddd.domain;

import java.util.Date;

public class Issue {
    
    public enum Status {
        OPEN, ASSIGNED, RESOLVED, CLOSED
    }
    public enum Resolution {
        FIXED, DUPLICATE, WONT_FIX, CANNOT_REPRODUCE
    }
    public enum RelationshipType {
        DUPLICATES, IS_DUPLICATED_BY, 
        REFERS_TO, IS_REFERRED_BY,
        BLOCKS, IS_BLOCKED_BY
    }

    private class RelatedIssue {

        private IssueNumber related;
        private RelationshipType type;
        
    }
    
    private IssueNumber number;
    private String title;
    private String description;
    
    private Date createdAt;
    
    private ProductVersion occuredIn;
    private ProductVersion fixedIn;
    
    private Status status;
    private Resolution resolution;
    
    private ParticipantID reportedBy;
    private ParticipantID assignedTo;
    
//    public void renameTo(String newName);
//    public void updateDescription(String newDescription);    
    
      // state transitions:    
//    public void assignTo(ParticipantID assignee);

      // resolve:
//    public void fixedIn(ProductVersion version);
//    public void duplicateOf(IssueNumber duplicate);    
//    public void wontFix(String reason);    
//    public void cannotReproduce();
    
//    public void close();
//    public void reopen(ProductVersion version);    
    
      // misc:
//      public void referTo(IssueNumber refered);
//      public void blocks(IssueNumber blocked);    
//      protected void isDuplicatedBy(IssueNumber duplicate);
//      protected void isReferredBy(IssueNumber referee);
//      protected void isBlockedBy(IssueNumber blocker);    
}
