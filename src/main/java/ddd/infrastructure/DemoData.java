package ddd.infrastructure;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import ddd.domain.Issue;
import ddd.domain.IssueFactory;
import ddd.domain.IssueRepository;
import ddd.domain.ParticipantID;
import ddd.domain.ProductVersion;

@Singleton
@Startup
public class DemoData {

    @Inject
    private IssueRepository repository;
    
    @Inject
    private IssueFactory factory;
    
    @PostConstruct
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void populateDemo(){

        System.out.println("-- Populating demo data");
        
        Issue renaming = factory.newBug(
                "Renaming issue", 
                "After double clicking on issue name there should be in-place edit and posibility to rename issue", 
                ProductVersion.of("ddd-issues 0.3.0")
            );        
        renaming.assignTo(new ParticipantID("homer.simpson"));
        renaming.fixedIn(ProductVersion.of("ddd-issues 0.4.0"));
        renaming.close();        
        repository.store(renaming);
        
        Issue assigning = factory.newBug(
                "Assigning participant", 
                "It should be possible to assign participat to Issue", 
                ProductVersion.of("ddd-issues 0.5.0")
            );
        assigning.assignTo(new ParticipantID("bart.simpson"));
        repository.store(assigning);
        
        Issue closing = factory.newBug(
                "Closing issue", 
                "If issue is already resolved it should be possible to close issue", 
                ProductVersion.of("ddd-issues 0.5.0")
            );        
        repository.store(closing);

        Issue fixing = factory.newBug(
                "Providing fix version for issue", 
                "When fixing issue it should be possible to assign product version in which it was fixed", 
                ProductVersion.of("ddd-issues 0.5.0")
            );        
        repository.store(fixing);
    }
    
}
