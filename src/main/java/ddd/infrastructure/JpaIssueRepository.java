package ddd.infrastructure;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ddd.domain.Issue;
import ddd.domain.IssueNumber;
import ddd.domain.IssueRepository;
import ddd.domain.Issues;

public class JpaIssueRepository implements IssueRepository {

    @PersistenceContext(unitName="issues-unit")
    private EntityManager entityManager;

    @Override
    public void store(Issue issue) {
        entityManager.persist(issue);
    }

    @Override
    public Issue load(IssueNumber issueNumber) {
        return entityManager.find(Issue.class, issueNumber);
    }

    @Override
    public Issues loadAll() {
        return null;
    }
}
