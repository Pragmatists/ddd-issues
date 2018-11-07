package ddd.infrastructure;

import ddd.domain.Issue;
import ddd.domain.IssueNumber;
import ddd.domain.IssueRepository;
import ddd.domain.Issues;

import java.util.HashMap;
import java.util.Map;

public class InMemoryIssueRepository implements IssueRepository {

    private Map<IssueNumber, Issue> storage = new HashMap<>();

    @Override
    public void store(Issue issue) {
        storage.put(issue.number(), issue);
    }

    @Override
    public Issue load(IssueNumber issueNumber) {
        Issue issue = storage.get(issueNumber);
        if(issue == null){
            throw new IssueNotFound(issueNumber);
        }
        return issue;
    }

    @Override
    public Issues loadAll() {
        return null;
    }
}
