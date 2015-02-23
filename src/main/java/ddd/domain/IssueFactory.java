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
      Issue newIssue = new Issue(sequence.nextNumber(), title, occuredIn, clock.time());
      newIssue.updateDescription(description);
      return newIssue;
    }
    
}
