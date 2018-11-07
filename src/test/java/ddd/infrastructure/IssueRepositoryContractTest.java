package ddd.infrastructure;

import ddd.domain.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public abstract class IssueRepositoryContractTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    protected IssueRepository repository;

    @Test
    public void shouldStoreAndLoadIssue() throws Exception {

        // given:
        Issue issue = anIssue(new IssueNumber(123));

        // when:
        repository.store(issue);

        // then:
        Issue loaded = repository.load(new IssueNumber(123));
        assertThat(loaded).isEqualToComparingFieldByFieldRecursively(issue);
    }

    @Test
    public void shouldStoreAndLoadIssue_complexCase() throws Exception {

        // given:
        Issue issue = anIssue(new IssueNumber(123));
        issue.wontFix("This is not a bug!");

        // when:
        repository.store(issue);

        // then:
        Issue loaded = repository.load(new IssueNumber(123));
        assertThat(loaded).isEqualToComparingFieldByFieldRecursively(issue);
    }

    @Test
    public void shouldStoreAndLoadIssue_hasRelatedIssue() throws Exception {

        // given:
        Issue issue = anIssue(new IssueNumber(123));
        issue.referTo(new IssueNumber(456));

        // when:
        repository.store(issue);

        // then:
        Issue loaded = repository.load(new IssueNumber(123));
        assertThat(loaded).isEqualToComparingFieldByFieldRecursively(issue);
    }

    @Test
    public void shouldFailMeaningfullyIfIssueWithGivenNumberAlreadyExist() throws Exception {

        // expect:
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Issue with number='123' already exists!");

        // given:
        issueAlreadyExists(new IssueNumber(123));

        // when:
        repository.store(anIssue(new IssueNumber(123)));
    }

    @Test
    public void shouldFailMeaningfullyIfIssueWithGivenNumberDoesNotExist() throws Exception {

        // expect:
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Issue with number='404' does not exist!");

        // when:
        repository.load(new IssueNumber(404));
    }


    @Test
    public void shouldFindAllIssues() {

        //given
        Issue issue1 = anIssue(new IssueNumber(123));
        Issue issue2 = anIssue(new IssueNumber(124));
        Issue issue3 = anIssue(new IssueNumber(125));

        repository.store(issue1);
        repository.store(issue2);
        repository.store(issue3);

        //when
        Issues issues = repository.loadAll();

        //then
        assertThat(issues)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(issue1, issue2, issue3);
    }


    @Test
    public void shouldFindIssuesInState() {

        //given
        Issue issue1 = anIssue(new IssueNumber(123));
        issue1.fixedIn(ProductVersion.of("product 1.2.3"));
        Issue issue2 = anIssue(new IssueNumber(124));
        issue2.fixedIn(ProductVersion.of("product 1.2.3"));
        issue2.close();
        Issue issue3 = anIssue(new IssueNumber(125));

        repository.store(issue1);
        repository.store(issue2);
        repository.store(issue3);

        //when
        Issues issues = repository.loadAll().inStatus(Issue.Status.CLOSED);

        //then
        assertThat(issues)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(issue2);
    }



    private Issue anIssue(IssueNumber issueNumber) {
        Issue issue = new Issue(issueNumber, "System does not work!", aProductVersion(), aDate());
        issue.assignTo(new ParticipantID("homer.simpson"));
        issue.fixedIn(aProductVersion("4.5.6"));
        issue.updateDescription("This is very very long description of this issue!");
        issue.referTo(new IssueNumber(888));
        issue.blocks(new IssueNumber(777));
        return issue;
    }

    private Issue issueAlreadyExists(IssueNumber issueNumber) {
        Issue issue = anIssue(issueNumber);
        repository.store(issue);
        return issue;
    }

    private Date aDate() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-10-11 12:34:56");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private ProductVersion aProductVersion() {
        return aProductVersion("1.2.3");
    }

    private ProductVersion aProductVersion(String version) {
        return new ProductVersion(new ProductID("buggy-app"), version);
    }
}
