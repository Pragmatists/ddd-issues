package ddd.application;

import ddd.domain.Issue;
import ddd.domain.IssueRepository;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

public class IssueResourceBaseTest extends EndToEndTest {

    @Autowired
    protected IssueRepository repository;
    @Autowired
    private EntityManager entityManager;

    @Before
    public void setUp() {
        clearIssues();
    }

    private void clearIssues() {
        doInTransaction(() -> {
            entityManager.createQuery("delete from ddd.domain.Issue").executeUpdate();
        });
    }

    protected void storeIssue(Issue issue) {
        doInTransaction(() -> {
            repository.store(issue);
        });
    }
}
