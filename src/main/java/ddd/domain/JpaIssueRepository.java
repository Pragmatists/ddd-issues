package ddd.domain;

import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class JpaIssueRepository implements IssueRepository {

    @PersistenceContext(unitName="issues-unit")
    private EntityManager entityManager;

    @Override
    public void store(Issue issue) {
        entityManager.persist(issue);
    }

    @Override
    public Optional<Issue> load(IssueNumber issueNumber) {
        return null;
    }

    @Override
    public Issues loadAll() {
        return null;
    }
}
