package ddd.application;

import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.junit.runner.RunWith;

@RunWith(Suite.class)
@SuiteClasses(value={IssuesResourceListing.class, IssueResourceRenaming.class})
public class EndToEndSuiteTest extends EndToEndTest {

}
