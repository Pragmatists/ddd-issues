package ddd.infrastructure;

import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import ddd.domain.Issue;
import ddd.domain.Issues;
import ddd.domain.ParticipantID;
import ddd.domain.ProductVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class JpaIssues implements Issues {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Issues forVersion(ProductVersion version) {
        return null;
    }

    @Override
    public Issues reportedBy(ParticipantID reporter) {
        return null;
    }

    @Override
    public Issues inStatus(Issue.Status... statuses) {
        return null;
    }

    @Override
    public Iterator<Issue> iterator() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Issue> query = criteriaBuilder.createQuery(Issue.class);
        query.from(Issue.class);
        return entityManager.createQuery(query).getResultList().iterator();
    }
}
