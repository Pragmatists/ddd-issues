package ddd.domain;

import static junitparams.JUnitParamsRunner.$;
import static org.assertj.core.api.Assertions.assertThat;

public class IssueNumberIsValueObject extends ValueObjectContractTest {

    @Override
    protected Object instance() {
        return new IssueNumber(42);
    }

    @Override
    protected Object[] equalInstances() {
        return $(new IssueNumber(42));
    }

    @Override
    protected Object[] nonEqualInstances() {
        return $(new IssueNumber(43));
    }

    @Override
    public void shouldHaveDescriptiveRepresentation() {

        // given:
        IssueNumber issueNumber = new IssueNumber(42);
        
        // when:
        String representation = issueNumber.toString();
        
        // then:
        assertThat(representation).isEqualTo("Issue #42");
    }
    
}
