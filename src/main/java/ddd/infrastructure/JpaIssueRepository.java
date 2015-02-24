package ddd.infrastructure;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ddd.domain.Issue;
import ddd.domain.IssueNumber;
import ddd.domain.IssueRepository;
import ddd.domain.Issues;
import ddd.domain.IssueRepository.IssueAlreadyExists;

public class JpaIssueRepository implements IssueRepository {

    @PersistenceContext(unitName="issues-unit")
    private EntityManager entityManager;

    @Override
    public void store(Issue issue) {
        if(load(issue.number()) != null){
            throw new IssueAlreadyExists(issue.number());
        }
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
