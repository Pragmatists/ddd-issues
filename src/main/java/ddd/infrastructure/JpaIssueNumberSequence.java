package ddd.infrastructure;

import javax.persistence.EntityManager;

import ddd.domain.IssueNumber;
import ddd.domain.IssueNumberSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JpaIssueNumberSequence implements IssueNumberSequence {

    @Autowired
    private EntityManager entityManager;

    @Override
    public IssueNumber nextNumber() {

        IssueNumber currentId = entityManager.createQuery("select max(number) from Issue", IssueNumber.class).getSingleResult();
        return currentId == null ? new IssueNumber(1) : currentId.next();
    }
}
