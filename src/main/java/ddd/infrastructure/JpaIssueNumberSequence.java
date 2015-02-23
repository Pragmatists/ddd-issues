package ddd.infrastructure;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import ddd.domain.Issue;
import ddd.domain.IssueNumber;
import ddd.domain.IssueNumberSequence;

@Stateless
public class JpaIssueNumberSequence implements IssueNumberSequence {

    @PersistenceContext(unitName="issues-unit")
    private EntityManager entityManager;

    @Override
    public IssueNumber nextNumber() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
        Root<Issue> issue = query.from(Issue.class);
        Path<Object> number = issue.get("number");
        query.select(cb.max(number.<Integer>get("value")));
        Integer currentId = entityManager.createQuery(query).getSingleResult();
        return new IssueNumber(currentId == null ? 0: currentId).next();
    }
}
