package ddd.domain;

import java.util.UUID;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class RelatedIssue {
    
    public enum RelationshipType {
        DUPLICATES, IS_DUPLICATED_BY,
        REFERS_TO, IS_REFERRED_BY,
        BLOCKS, IS_BLOCKED_BY
    }

    @Id
    private String id = UUID.randomUUID().toString();

    @Embedded
    private IssueNumber related;
    private RelationshipType type;
    @ManyToOne
    private Issue source;
    
    protected RelatedIssue() {
        // JPA
    }
    
    protected RelatedIssue(Issue source, IssueNumber number, RelatedIssue.RelationshipType type) {
        this();
        this.source = source;
        this.related = number;
        this.type = type;
    }
}