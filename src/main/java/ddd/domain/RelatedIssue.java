package ddd.domain;

public class RelatedIssue {
    
    public enum RelationshipType {
        DUPLICATES, IS_DUPLICATED_BY,
        REFERS_TO, IS_REFERRED_BY,
        BLOCKS, IS_BLOCKED_BY
    }

    private IssueNumber related;
    private RelationshipType type;
    
    protected RelatedIssue() {
        // JPA
    }
    
    protected RelatedIssue(IssueNumber number, RelatedIssue.RelationshipType type) {
        this();
        this.related = number;
        this.type = type;
    }
    
    @Override
    public String toString() {
        return String.format("--[%s]--> %s", type, related);
    }
    
    @Override
    public int hashCode() {
        return related.hashCode() + type.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        
        if(!(obj instanceof RelatedIssue)){
            return false;
        }
        
        RelatedIssue other = (RelatedIssue) obj;
        
        return type.equals(other.type) && related.equals(other.related);
    }
    
}