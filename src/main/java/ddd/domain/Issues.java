package ddd.domain;


public interface Issues extends Iterable<Issue> {

    public Issues forVersion(ProductVersion version);
    public Issues reportedBy(ParticipantID reporter);
    public Issues inStatus(Issue.Status... statuses);

}
