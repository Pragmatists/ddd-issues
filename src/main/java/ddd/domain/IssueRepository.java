package ddd.domain;

import java.util.Optional;

public interface IssueRepository {

    public void store(Issue issue);

    public Optional<Issue> load(IssueNumber issueNumber);
    
    public Issues loadAll();
    
}
