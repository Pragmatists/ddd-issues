package ddd.application;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import ddd.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/issues")
public class IssueResource {

    @Autowired
    private IssueRepository repository;

    @Autowired
    private Issues issues;

    @GetMapping
    @Transactional(readOnly = true)
    public IssuesJson list() {
        
        IssuesJson json = new IssuesJson();
        issues.forEach(issue -> json.add(new ExistingIssueJson(issue)));
        
        return json;
    }

    @GetMapping("/{issueNumber}")
    @Transactional(readOnly = true)
    public ExistingIssueJson get(@PathVariable("issueNumber") Integer issueNumber) {
        
        Issue load = repository.load(new IssueNumber(issueNumber));
        return new ExistingIssueJson(load);
    }


    @GetMapping("/releaseNotes")
    @Transactional(readOnly = true)
    public ReleaseNotesReportResponseJson getReleaseNotes(@RequestParam("from") String from, @RequestParam("to") String to) {

        ReleaseNotesReportResponseJson json = new ReleaseNotesReportResponseJson();

        repository.loadAll().inStatus(Issue.Status.CLOSED).forEach(issue -> {
            json.addIssue(issue.number(), issue.fixVersion(), issue.title());
        });
        //Issue load = repository.load(new IssueNumber(issueNumber));
        return json;
    }

    @Transactional
    @PostMapping(path="/{issueNumber}/rename")
    public void rename(@PathVariable("issueNumber") Integer issueNumber, @RequestBody RenameIssueRequestJson json) {

        Issue issue = repository.load(new IssueNumber(issueNumber));
        issue.renameTo(json.newTitle);
    }

    @Transactional
    @PostMapping(path="/{issueNumber}/assignParticipant")
    public void rename(@PathVariable("issueNumber") Integer issueNumber, @RequestBody AssignExistingPaticipantRequestJson json) {

        Issue issue = repository.load(new IssueNumber(issueNumber));
        issue.assignTo(new ParticipantID(json.participantId));
    }



    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    static class RenameIssueRequestJson {
        
        String newTitle = "";
        
        public RenameIssueRequestJson() {}

        public RenameIssueRequestJson(String newTitle) {
            this();
            this.newTitle = newTitle;
        }
    }
    
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    static class ExistingIssueJson {

        String number;
        String title;
        String description;
        VersionJson occurredIn = new VersionJson();
        VersionJson fixedIn = new VersionJson();
        String status;
        String resolution;
        String createdAt;
        String assignee;

        public ExistingIssueJson() {}

        public ExistingIssueJson(Issue issue) {
            number = issue.number().toString();
            title = issue.title();
            description = issue.description();
            status = issue.status().name().toLowerCase();
            resolution = issue.resolution() == null ? null : issue.resolution().name().toLowerCase();
            createdAt = format(issue.createdAt());
            occurredIn = new VersionJson(issue.occuredIn());
            fixedIn = issue.fixVersion() == null ? null :  new VersionJson(issue.fixVersion());
            assignee = issue.assignee() == null ? null :  issue.assignee().toString();
        }

    }
    
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    static class VersionJson {
        
        String product;
        String version;

        public VersionJson() {}

        public VersionJson(ProductVersion productVersion) {
            product = productVersion.toString().split(" ")[0];
            version = productVersion.toString().split(" ")[1];
        }
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class IssuesJson extends ArrayList<ExistingIssueJson> {
    }

    private static String format(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static class AssignExistingPaticipantRequestJson {

        private String participantId;

        private AssignExistingPaticipantRequestJson() {
        }

        public AssignExistingPaticipantRequestJson(String participantId) {
            this();
            this.participantId = participantId;
        }

    }

    static class ReleaseNotesReportResponseJson extends ArrayList<ReleaseNoteRow> {
        public void addIssue(IssueNumber number, ProductVersion productVersion, String title) {
            add(new ReleaseNoteRow(number,productVersion,title));
        }
    }

    private static class ReleaseNoteRow {
        String number;
        String version;
        String title;

        public ReleaseNoteRow(IssueNumber number, ProductVersion productVersion, String title) {
            this.number = number.toString();
            this.version = productVersion != null ? productVersion.version() : "";
            this.title = title;
        }
    }
}
