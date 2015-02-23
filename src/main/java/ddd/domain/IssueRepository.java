package ddd.domain;

public interface IssueRepository {

    public void store(Issue issue);

    public Issue load(IssueNumber issueNumber);
    
    public Issues loadAll();
    
}
