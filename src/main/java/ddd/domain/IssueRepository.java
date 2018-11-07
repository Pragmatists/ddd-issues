package ddd.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface IssueRepository {

    public class IssueAlreadyExists extends IllegalArgumentException {

        public IssueAlreadyExists(IssueNumber number) {
            super(String.format("Issue with number='%s' already exists!", number));
        }
    }

    public class IssueNotFound extends IllegalArgumentException {
        
        public IssueNotFound(IssueNumber number) {
            super(String.format("Issue with number='%s' does not exist!", number));
        }
    }
    
    public void store(Issue issue);

    public Issue load(IssueNumber issueNumber);
    
    public Issues loadAll();

}
