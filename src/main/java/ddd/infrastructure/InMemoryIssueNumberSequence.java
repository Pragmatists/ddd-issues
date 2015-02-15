package ddd.infrastructure;

import ddd.domain.IssueNumber;
import ddd.domain.IssueNumberSequence;

import javax.ejb.Stateless;
import java.util.concurrent.atomic.AtomicInteger;

@Stateless
public class InMemoryIssueNumberSequence implements IssueNumberSequence {

    AtomicInteger atomicInteger = new AtomicInteger();

    @Override
    public IssueNumber nextNumber() {
        return new IssueNumber(atomicInteger.incrementAndGet());
    }
}
