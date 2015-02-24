package ddd.domain;

import javax.ejb.ApplicationException;

public interface IssueRepository {

    @ApplicationException
    public class IssueAlreadyExists extends IllegalArgumentException {

        public IssueAlreadyExists(IssueNumber number) {
            super(String.format("Issue with number='%s' already exists!", number));
        }
    }

    public void store(Issue issue);

    public Issue load(IssueNumber issueNumber);
    
    public Issues loadAll();

}
