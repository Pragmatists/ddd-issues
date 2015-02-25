package ddd.infrastructure;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import ddd.domain.Issue;
import ddd.domain.IssueNumber;
import ddd.domain.IssueRepository;
import ddd.domain.ParticipantID;
import ddd.domain.ProductVersion;

@Singleton
@Startup
public class DemoData {

    @Inject
    private IssueRepository repository;
    
    @PostConstruct
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void populateDemo(){

        System.out.println("-- Populating demo data");
        
        Issue renaming = newIssue(1,
                "Renaming issue", 
                "After double clicking on issue name there should be in-place edit and posibility to rename issue", 
                ProductVersion.of("ddd-issues 0.3.0"),
                aDate("2015-01-01 13:34:34")
            );        
        renaming.assignTo(new ParticipantID("homer.simpson"));
        renaming.fixedIn(ProductVersion.of("ddd-issues 0.4.0"));
        renaming.close();        
        repository.store(renaming);
        
        Issue assigning = newIssue(2,
                "Assigning participant", 
                "It should be possible to assign participat to Issue", 
                ProductVersion.of("ddd-issues 0.5.0"),
                aDate("2015-01-01 21:44:00")
            );
        assigning.assignTo(new ParticipantID("bart.simpson"));
        repository.store(assigning);
        
        Issue closing = newIssue(3,
                "Closing issue", 
                "If issue is already resolved it should be possible to close issue", 
                ProductVersion.of("ddd-issues 0.5.0"),
                aDate("2015-01-02 10:00:12")
            );        
        repository.store(closing);

        Issue fixing = newIssue(4,
                "Providing fix version for issue", 
                "When fixing issue it should be possible to assign product version in which it was fixed", 
                ProductVersion.of("ddd-issues 0.5.0"),
                aDate("2015-01-21 17:24:00")
            );        
        repository.store(fixing);
    }

    private Date aDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Issue newIssue(int issueNumber, String title, String description, ProductVersion occurredIn, Date createdAt) {
        Issue issue = new Issue(new IssueNumber(issueNumber), title, occurredIn, createdAt);
        issue.updateDescription(description);
        return issue;
    }
    
}
