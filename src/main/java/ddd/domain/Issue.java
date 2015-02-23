package ddd.domain;

import java.util.Date;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
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

    @EmbeddedId
    private IssueNumber number;
    private String title;
    private String description;

    private Date createdAt;

    public Date createdAt() {
        return createdAt;
    }


    @Embedded
    private ProductVersion occuredIn;
//    private ProductVersion fixedIn;

//    private Status status;
//    private Resolution resolution;

//    private ParticipantID reportedBy;
//    private ParticipantID assignedTo;

    public Issue() {
    }

    public Issue(IssueNumber number, String title, ProductVersion occuredIn, Date createdAt) {
        this.number = number;
        this.title = title;
        this.occuredIn = occuredIn;
        this.createdAt = createdAt;
    }
    public IssueNumber number() {
        return number;
    }


    public ProductVersion occuredIn() {
        return occuredIn;
    }

    public void renameTo(String newName) {
        this.title = newName;
    }

    public String title() {
        return title;
    }

    public String description() {
        return description;
    }

    public void updateDescription(String newDescription){
        description = newDescription;
    }

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
