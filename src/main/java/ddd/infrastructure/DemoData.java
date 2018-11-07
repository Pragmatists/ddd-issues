package ddd.infrastructure;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;

import ddd.domain.Issue;
import ddd.domain.IssueNumber;
import ddd.domain.IssueRepository;
import ddd.domain.ParticipantID;
import ddd.domain.ProductVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("demo")
public class DemoData {

    @Autowired
    private IssueRepository repository;
    
    @EventListener(ContextRefreshedEvent.class)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
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
        
        Issue closing = newIssue(2,
                "Creating new issue", 
                "It should be possible to create a new issue. New issue is created by providing issue title and product version which " +
                "is affected by this issue. After successfull creation, issue details should be displayed.", 
                ProductVersion.of("ddd-issues 0.5.0"),
                aDate("2015-01-02 10:00:12")
            );        
        repository.store(closing);

        Issue assigning = newIssue(3,
                "Assigning participant", 
                "It should be possible to assign participat to Issue", 
                ProductVersion.of("ddd-issues 0.5.0"),
                aDate("2015-01-01 21:44:00")
                );
        assigning.assignTo(new ParticipantID("bart.simpson"));
        repository.store(assigning);
        
        Issue fixing = newIssue(4,
                "Providing fix version for issue", 
                "When fixing issue it should be possible to assign product version in which it was fixed", 
                ProductVersion.of("ddd-issues 0.5.0"),
                aDate("2015-01-21 17:24:00")
            );        
        repository.store(fixing);

        Issue releaseNotes = newIssue(5,
                "Generate release notes for given version range", 
                "It should be possible to generate release notes for given version range. Release notes should contain titles of every " + 
                "tickets fixed in given version range. Titles should be grouped by versions. Versions should be displayed in descending order.", 
                ProductVersion.of("ddd-issues 0.5.0"),
                aDate("2015-01-24 15:33:01")
            );        
        repository.store(releaseNotes);

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
