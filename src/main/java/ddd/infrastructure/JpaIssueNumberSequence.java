package ddd.infrastructure;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ddd.domain.IssueNumber;
import ddd.domain.IssueNumberSequence;

@Stateless
public class JpaIssueNumberSequence implements IssueNumberSequence {

    @PersistenceContext(unitName="issues-unit")
    private EntityManager entityManager;

    @Override
    public IssueNumber nextNumber() {

        IssueNumber currentId = entityManager.createQuery("select max(number) from Issue", IssueNumber.class).getSingleResult();
        return currentId == null ? new IssueNumber(1) : currentId.next();
    }
}
