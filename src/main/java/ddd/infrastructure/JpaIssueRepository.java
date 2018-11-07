package ddd.infrastructure;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ddd.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class JpaIssueRepository implements IssueRepository {

    @Autowired
    private EntityManager entityManager;

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

        return new Issues() {
            private Issue.Status[] statuses = Issue.Status.values();

            @Override
            public Issues forVersion(ProductVersion version) {
                return this;
            }

            @Override
            public Issues reportedBy(ParticipantID reporter) {
                return this;
            }

            @Override
            public Issues inStatus(Issue.Status... statuses) {
                this.statuses = statuses;
                return this;
            }

            @Override
            public Iterator<Issue> iterator() {
                return entityManager.createQuery("from Issue i where i.status in :statuses", Issue.class)
                        .setParameter("statuses", Arrays.asList(statuses))
                        .getResultList().iterator();
            }
        };
    }

}
