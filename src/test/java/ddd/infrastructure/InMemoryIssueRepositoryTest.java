package ddd.infrastructure;

import org.junit.Before;

public class InMemoryIssueRepositoryTest extends IssueRepositoryContractTest {

    @Before
    public void setUp() throws Exception {
        repository = new InMemoryIssueRepository();
    }
}
