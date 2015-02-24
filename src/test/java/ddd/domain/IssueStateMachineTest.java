package ddd.domain;

import static junitparams.JUnitParamsRunner.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

import ddd.domain.Issue.Status;

@RunWith(JUnitParamsRunner.class)
public class IssueStateMachineTest {

    @Test
    @Parameters(method="validTransitions")
    public void shouldTransitToState(Issue issue, Consumer<Issue> action, Status expectedState) throws Exception {

        // given:
        
        // when:
        action.accept(issue);
        
        // then:
        assertThat(issue.status()).isEqualTo(expectedState);
        
    }
    
    @Test
    @Parameters(method="invalidTransitions")
    public void shouldProtectFromInvalidTransition(Issue issue, Consumer<Issue> action) throws Exception {
        
        // given:
        Status initialState = issue.status();
        
        try{
            
            // when:
            action.accept(issue);
            failBecauseExceptionWasNotThrown(IllegalStateException.class);
            
        } catch(Exception e){
            
            // then:
            assertThat(issue.status()).isEqualTo(initialState);
            assertThat(e)
                .isInstanceOf(IllegalStateException.class);
        }

    }
    
    public Object[] validTransitions(){
        return $(
                $(openIssue(), transitionUsing(this::assign), Status.ASSIGNED),
                $(openIssue(), transitionUsing(this::fix), Status.RESOLVED),
                $(openIssue(), transitionUsing(this::markAsDuplicate), Status.RESOLVED),
                $(openIssue(), transitionUsing(this::markAsWontFix), Status.RESOLVED),
                $(openIssue(), transitionUsing(this::cannotReproduce), Status.RESOLVED),

                $(assignedIssue(), transitionUsing(this::assign), Status.ASSIGNED),
                $(assignedIssue(), transitionUsing(this::fix), Status.RESOLVED),
                $(assignedIssue(), transitionUsing(this::markAsDuplicate), Status.RESOLVED),
                $(assignedIssue(), transitionUsing(this::markAsWontFix), Status.RESOLVED),
                
                $(resolvedIssue(), transitionUsing(this::assign), Status.RESOLVED),
                $(resolvedIssue(), transitionUsing(this::fix), Status.RESOLVED),
                $(resolvedIssue(), transitionUsing(this::markAsDuplicate), Status.RESOLVED),
                $(resolvedIssue(), transitionUsing(this::markAsWontFix), Status.RESOLVED),

                $(resolvedIssue(), transitionUsing(this::close), Status.CLOSED),
                $(resolvedIssue(), transitionUsing(this::reopen), Status.OPEN),
                
                $(closedIssue(), transitionUsing(this::reopen), Status.OPEN)
               );
    }
    
    public Object[] invalidTransitions(){

        return $(
                $(openIssue(), transitionUsing(this::close)),
                $(openIssue(), transitionUsing(this::reopen)),
                
                $(assignedIssue(), transitionUsing(this::close)),
                $(assignedIssue(), transitionUsing(this::reopen)),
                
                $(closedIssue(), transitionUsing(this::assign)),
                $(closedIssue(), transitionUsing(this::close)),
                $(closedIssue(), transitionUsing(this::fix)),
                $(closedIssue(), transitionUsing(this::markAsDuplicate)),
                $(closedIssue(), transitionUsing(this::markAsWontFix))
               );
    }
    
    // --
    
    private Consumer<Issue> transitionUsing(Consumer<Issue> lambda) {
        return lambda;
    }
    
    private void assign(Issue issue) {
        issue.assignTo(new ParticipantID("homer.simpson"));
    }

    private void close(Issue issue) {
        issue.close();
    }

    private void fix(Issue issue) {
        issue.fixedIn(aProductVersion());
    }
    
    private void markAsDuplicate(Issue issue) {
        issue.duplicateOf(new IssueNumber(787));
    }

    private void markAsWontFix(Issue issue) {
        issue.wontFix("Works as designed!");
    }

    private void cannotReproduce(Issue issue) {
        issue.cannotReproduce();
    }

    private void reopen(Issue issue) {
        issue.reopen(aProductVersion());
    }
    
    private ParticipantID aParticipant() {
        return new ParticipantID("homer.simpson");
    }
    
    private Issue openIssue() {
        return new Issue(new IssueNumber(123), "Issue Title", aProductVersion(), aDate());
    }

    private Issue resolvedIssue() {
        Issue issue = openIssue();
        issue.assignTo(aParticipant());
        issue.fixedIn(aProductVersion());
        return issue;
    }

    private Issue assignedIssue() {
        Issue issue = openIssue();
        issue.assignTo(aParticipant());
        return issue;
    }

    private Issue closedIssue() {
        Issue issue = resolvedIssue();
        issue.close();
        return issue;
    }
    
    private static Date aDate() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-11");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private ProductVersion aProductVersion() {
        return aProductVersion("buggy", "3.2.1");
    }

    private ProductVersion aProductVersion(String product, String version) {
        return new ProductVersion(new ProductID(product), version);
    }
    
}
