package ddd.domain;

import static org.assertj.core.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ddd.domain.Issue.Resolution;
import ddd.domain.Issue.Status;
import ddd.domain.RelatedIssue.RelationshipType;

public class IssueTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Test
    public void shouldBeInOpenStateJustAfterCreation() throws Exception {

        // given:
        
        // when:
        Issue issue = newIssue();
        
        // then:
        assertThat(issue.status()).isEqualTo(Status.OPEN);
    }
    
    @Test
    public void shouldBeInAssignedStateAfterAssigningParticipant() throws Exception {
        
        // given:
        Issue issue = newIssue();
        
        // when:
        issue.assignTo(aParticipant());
        
        // then:
        assertThat(issue.status()).isEqualTo(Status.ASSIGNED);
    }
    
    @Test
    public void shouldFailMeaningfullyIfAssigningInvalidParticipant() throws Exception {
        
        // expect:
        thrown.expect(IllegalArgumentException.class);
        
        // given:
        Issue issue = newIssue();
        
        // when:
        issue.assignTo(null);
    }
    
    @Test
    public void shouldHaveAssigneeAfterAssigningParticipant() throws Exception {
        
        // given:
        Issue issue = newIssue();
        
        // when:
        issue.assignTo(aParticipant("homer.simpson"));
        
        // then:
        assertThat(issue.assignee()).isEqualTo(aParticipant("homer.simpson"));
    }
    
    @Test
    public void shouldBeInResolvedStateAfterFixing() throws Exception {
        
        // given:
        Issue issue = newIssue();
        
        // when:
        issue.fixedIn(aProductVersion());
        
        // then:
        assertThat(issue.status()).isEqualTo(Status.RESOLVED);
    }
    
    @Test
    public void shouldFailMeaningfullyIfAssigningInvalidFixVersion() throws Exception {
        
        // expect:
        thrown.expect(IllegalArgumentException.class);
        
        // given:
        Issue issue = newIssue();
        
        // when:
        issue.fixedIn(null);
    }
    
    @Test
    public void shouldHaveResolutionAfterFixing() throws Exception {
        
        // given:
        Issue issue = newIssue();
        
        // when:
        issue.fixedIn(aProductVersion());
        
        // then:
        assertThat(issue.resolution()).isEqualTo(Resolution.FIXED);
    }
    
    @Test
    public void shouldHaveFixVersionAfterFixing() throws Exception {
        
        // given:
        Issue issue = newIssue();
        
        // when:
        issue.fixedIn(aProductVersion("buggy-app", "3.2.1"));
        
        // then:
        assertThat(issue.fixVersion()).isEqualTo(aProductVersion("buggy-app", "3.2.1"));
    }
    
    @Test
    public void shouldBeInResolvedStateAfterMarkingAsDuplicate() throws Exception {
        
        // given:
        Issue issue = newIssue();
        
        // when:
        issue.duplicateOf(new IssueNumber(987));
        
        // then:
        assertThat(issue.status()).isEqualTo(Status.RESOLVED);
    }

    @Test
    public void shouldFailMeaningfullyIfMarkingAsDuplicateOfInvalidIssue() throws Exception {
        
        // expect:
        thrown.expect(IllegalArgumentException.class);
        
        // given:
        Issue issue = newIssue();
        
        // when:
        issue.duplicateOf(null);
    }

    @Test
    public void shouldHaveResolutionAfterMarkingAsDuplicate() throws Exception {
        
        // given:
        Issue issue = newIssue();
        
        // when:
        issue.duplicateOf(new IssueNumber(987));
        
        // then:
        assertThat(issue.resolution()).isEqualTo(Resolution.DUPLICATE);
    }
    
    @Test
    public void shouldHaveRelatedIssuesAfterMarkingAsDuplicate() throws Exception {
        
        // given:
        Issue issue = newIssue();
        
        // when:
        issue.duplicateOf(new IssueNumber(987));
        
        // then:
        assertThat(issue.isDuplicateOf(new IssueNumber(987))).isTrue();
    }
    
    @Test
    public void shouldBeInResolvedStateAfterMarkingAsCannotReproduce() throws Exception {
        
        // given:
        Issue issue = newIssue();
        
        // when:
        issue.cannotReproduce();
        
        // then:
        assertThat(issue.status()).isEqualTo(Status.RESOLVED);
    }

    @Test
    public void shouldHaveResolutionAfterMarkingAsCannotReproduce() throws Exception {
        
        // given:
        Issue issue = newIssue();
        
        // when:
        issue.cannotReproduce();
        
        // then:
        assertThat(issue.resolution()).isEqualTo(Resolution.CANNOT_REPRODUCE);
    }
    
    @Test
    public void shouldBeInResolvedStateAfterMarkingAsWontFix() throws Exception {
        
        // given:
        Issue issue = newIssue();
        
        // when:
        issue.wontFix("Works this way by design.");
        
        // then:
        assertThat(issue.status()).isEqualTo(Status.RESOLVED);
    }

    @Test
    public void shouldFailMeaningfullyIfProvidingInvalidExplanation() throws Exception {
        
        // expect:
        thrown.expect(IllegalArgumentException.class);
        
        // given:
        Issue issue = newIssue();
        
        // when:
        issue.wontFix(null);
    }

    @Test
    public void shouldFailMeaningfullyIfProvidingEmptyExplanation() throws Exception {
        
        // expect:
        thrown.expect(IllegalArgumentException.class);
        
        // given:
        Issue issue = newIssue();
        
        // when:
        issue.wontFix("");
    }
    
    @Test
    public void shouldHaveResolutionAfterMarkingAsWontFix() throws Exception {
        
        // given:
        Issue issue = newIssue();
        
        // when:
        issue.wontFix("Works this way by design.");
        
        // then:
        assertThat(issue.resolution()).isEqualTo(Resolution.WONT_FIX);
    }
    
    @Test
    public void shouldBeInClosedStateAfterClosing() throws Exception {
        
        // given:
        Issue issue = resolvedIssue();
        
        // when:
        issue.close();
        
        // then:
        assertThat(issue.status()).isEqualTo(Status.CLOSED);
    }
    
    @Test
    public void shouldGoBackToOpenStateAfterReOpening() throws Exception {
        
        // given:
        Issue issue = closedIssue();
        
        // when:
        issue.reopen(aProductVersion());
        
        // then:
        assertThat(issue.status()).isEqualTo(Status.OPEN);
    }

    @Test
    public void shouldFailMeaningfullyIfAssigningInvalidFixVersionOnReopen() throws Exception {
        
        // expect:
        thrown.expect(IllegalArgumentException.class);
        
        // given:
        Issue issue = closedIssue();
        
        // when:
        issue.reopen(null);
    }
        
    @Test
    public void shouldResetOccuredInAfterReOpening() throws Exception {
        
        // given:
        Issue issue = closedIssue();
        
        // when:
        issue.reopen(aProductVersion("buggy-app", "5.5.5"));
        
        // then:
        assertThat(issue.occuredIn()).isEqualTo(aProductVersion("buggy-app", "5.5.5"));
    }

    @Test
    public void shouldResetAssigneeAfterReOpening() throws Exception {
        
        // given:
        Issue issue = resolvedIssue();
        issue.assignTo(aParticipant());
        
        // when:
        issue.reopen(aProductVersion("buggy-app", "5.5.5"));
        
        // then:
        assertThat(issue.assignee()).isNull();
    }
    
    @Test
    public void shouldHaveRelatedIssuesAfterMarkingAsBlocker() throws Exception {
        
        // given:
        Issue issue = newIssue();
        
        // when:
        issue.blocks(new IssueNumber(987));
        
        // then:
        assertThat(issue.hasRelationshipTo(new IssueNumber(987), RelatedIssue.RelationshipType.BLOCKS)).isTrue();
    }
    
    @Test
    public void shouldHaveRelatedIssuesAfterReferringToOtherIssue() throws Exception {
        
        // given:
        Issue issue = newIssue();
        
        // when:
        issue.referTo(new IssueNumber(987));
        
        // then:
        assertThat(issue.hasRelationshipTo(new IssueNumber(987), RelatedIssue.RelationshipType.REFERS_TO)).isTrue();
    }
    
    @Test
    public void shouldFailMeaningfullyIfReferingToInvalidIssue() throws Exception {
        
        // expect:
        thrown.expect(IllegalArgumentException.class);
        
        // given:
        Issue issue = newIssue();
        
        // when:
        issue.referTo(null);
    }

    @Test
    public void shouldFailMeaningfullyIfBlocksInvalidIssue() throws Exception {
        
        // expect:
        thrown.expect(IllegalArgumentException.class);
        
        // given:
        Issue issue = newIssue();
        
        // when:
        issue.blocks(null);
    }
    
    // --
    
    private ParticipantID aParticipant() {
        return new ParticipantID("homer.simpson");
    }
    
    private ParticipantID aParticipant(String participant) {
        return new ParticipantID(participant);
    }

    private Issue newIssue() {
        return new Issue(new IssueNumber(123), "Issue Title", aProductVersion(), aDate());
    }

    private Issue resolvedIssue() {
        Issue issue = newIssue();
        issue.fixedIn(aProductVersion());
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
