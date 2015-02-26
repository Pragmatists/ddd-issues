package ddd.infrastructure;

import javax.persistence.EntityManager;

import ddd.domain.Issue;
import ddd.domain.IssueNumber;
import ddd.domain.IssueRepository;
import ddd.domain.Issues;

public class JpaIssueRepository implements IssueRepository {

    public JpaIssueRepository() {
    }

    public JpaIssueRepository(EntityManager entityManager) {
    }

    @Override
    public void store(Issue issue) {
        
    }

    @Override
    public Issue load(IssueNumber issueNumber) {
        return null;
    }

    @Override
    public Issues loadAll() {
        return null;
    }

}