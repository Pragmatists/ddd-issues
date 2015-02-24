package ddd.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.apache.openjpa.persistence.jdbc.Strategy;

@Entity
public class Issue {

    public enum Status {

        OPEN {
            protected Status assigned() { return ASSIGNED; }
            protected Status resolve() { return RESOLVED; }
        }, 
        ASSIGNED {
            protected Status assigned() { return this; }
            protected Status resolve() { return RESOLVED; }
        }, 
        RESOLVED {
            protected Status assigned() { return this; }
            protected Status resolve() { return this; }
            protected Status close() { return CLOSED; }
            protected Status reopen() { return OPEN; }
        }, 
        CLOSED {
            protected Status reopen() { return OPEN; }
        };

        private Status transitionNotAllowed(String action) {
            throw new IllegalStateException(String.format("Cannot %s already %s issue!", action, this));
        }
        
        protected Status assigned() {
            return transitionNotAllowed("assign");
        }

        protected Status resolve() {
            return transitionNotAllowed("resolve");
        }

        protected Status close() {
            return transitionNotAllowed("close");
        }

        protected Status reopen() {
            return transitionNotAllowed("reopen");
        }
    }

    public enum Resolution {
        FIXED, DUPLICATE, WONT_FIX, CANNOT_REPRODUCE
    }

    @EmbeddedId
    private IssueNumber number;
    private String title;
    private String description;

    private Status status;
    private Resolution resolution;
    
    private Date createdAt;

    @Strategy("ddd.infrastructure.ProductVersionSerializationStrategy")
    private ProductVersion occuredIn;
    @Strategy("ddd.infrastructure.ProductVersionSerializationStrategy")
    private ProductVersion fixVersion;
    @Strategy("ddd.infrastructure.ParticipantIDSerializationStrategy")
    private ParticipantID assignee;

    @OneToMany(cascade=CascadeType.ALL, mappedBy="source")
    private Set<RelatedIssue> relatedIssues = new HashSet<>();
    
    protected Issue() {
        this.status = Status.OPEN;
    }

    public Issue(IssueNumber number, String title, ProductVersion occuredIn, Date createdAt) {
        this();
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

    public Date createdAt() {
        return createdAt;
    }
    
    public void updateDescription(String newDescription){
        description = newDescription;
    }

    public Status status() {
        return status;
    }

    public ParticipantID assignee() {
        return assignee;
    }
    
    // state transitions:
    public void assignTo(ParticipantID assignee){
        
        this.status = status.assigned();
        this.assignee = assignee;
    }

    private void resolveAs(Resolution solution) {
        
        this.status = status.resolve();
        this.resolution = solution;
    }
    
    public void fixedIn(ProductVersion version){
        
        resolveAs(Resolution.FIXED);
        this.fixVersion = version;
    }

    public void duplicateOf(IssueNumber duplicate){
        
        resolveAs(Resolution.DUPLICATE);
        this.relatedIssues.add(new RelatedIssue(this, duplicate, RelatedIssue.RelationshipType.DUPLICATES));
    }
    
    public void wontFix(String reason){
        
        resolveAs(Resolution.WONT_FIX);
    }

    public void cannotReproduce(){
        
        resolveAs(Resolution.CANNOT_REPRODUCE);
    }

    public Resolution resolution() {
        return resolution;
    }

    public ProductVersion fixVersion() {
        return fixVersion;
    }

    public boolean isDuplicateOf(IssueNumber issueNumber) {
        return hasRelationshipTo(issueNumber, RelatedIssue.RelationshipType.DUPLICATES);
    }

    public void close(){
        this.status = status.close();
    }
    public void reopen(ProductVersion version){
        this.status = status.reopen();
        this.occuredIn = version;
        this.assignee = null;
    }

    public boolean hasRelationshipTo(IssueNumber issueNumber, RelatedIssue.RelationshipType type) {
        return relatedIssues.contains(new RelatedIssue(this, issueNumber, type));
    }

    public void blocks(IssueNumber issueNumber) {
        this.relatedIssues.add(new RelatedIssue(this, issueNumber, RelatedIssue.RelationshipType.BLOCKS));
    }
    public void referTo(IssueNumber issueNumber) {
        this.relatedIssues.add(new RelatedIssue(this, issueNumber, RelatedIssue.RelationshipType.REFERS_TO));
    }
    protected void isDuplicatedBy(IssueNumber duplicate){
        this.relatedIssues.add(new RelatedIssue(this, duplicate, RelatedIssue.RelationshipType.IS_DUPLICATED_BY));
    }
    protected void isReferredBy(IssueNumber referee){
        this.relatedIssues.add(new RelatedIssue(this, referee, RelatedIssue.RelationshipType.IS_REFERRED_BY));
    }
    protected void isBlockedBy(IssueNumber blocker){
        this.relatedIssues.add(new RelatedIssue(this, blocker, RelatedIssue.RelationshipType.IS_BLOCKED_BY));
    }
}
