package ddd.infrastructure;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ddd.domain.Issue;
import ddd.domain.IssueNumber;
import ddd.domain.IssueRepository;
import ddd.domain.Issues;

public class JpaIssueRepository implements IssueRepository {

    @PersistenceContext(unitName="issues-unit") 
    private EntityManager entityManager;

    public JpaIssueRepository() {
    }
    
    public JpaIssueRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void store(Issue issue) {
        
        find(issue.number()).ifPresent(i -> {
            throw new IssueAlreadyExists(i.number());
        });
        
        entityManager.persist(issue);
    }

    private Optional<Issue> find(IssueNumber issueNumber) {
        return Optional.ofNullable(entityManager.find(Issue.class, issueNumber));
    }
    
    @Override
    public Issue load(IssueNumber issueNumber) {
        return find(issueNumber).orElseThrow(() -> new IssueNotFound(issueNumber));
    }

    @Override
    public Issues loadAll() {
        return null;
    }

}
