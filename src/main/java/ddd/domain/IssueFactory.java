package ddd.domain;

import javax.inject.Inject;

public class IssueFactory {

//    private LoggedInParticipant loggedInParticipant;
    @Inject
    private Clock clock;
    @Inject
    private IssueNumberSequence sequence;
    
    public Issue newBug(String title, String description, ProductVersion occuredIn){

//      ParticipantID reportedBy = loggedInParticipant.participantID();        
      Issue newIssue = new Issue(sequence.nextNumber(), "", occuredIn, clock.time());
      newIssue.renameTo(title);
//      newIssue.updateDescription(description);        
      return newIssue;
    }
    
}
