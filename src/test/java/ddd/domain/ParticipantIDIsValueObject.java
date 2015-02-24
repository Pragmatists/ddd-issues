package ddd.domain;

import static junitparams.JUnitParamsRunner.$;
import static org.assertj.core.api.Assertions.assertThat;

public class ParticipantIDIsValueObject extends ValueObjectContractTest {

    @Override
    protected Object instance() {
        return new ParticipantID("homer.simpson");
    }

    @Override
    protected Object[] equalInstances() {
        return $(new ParticipantID("homer.simpson"));
    }

    @Override
    protected Object[] nonEqualInstances() {
        return $(new ParticipantID("bart.simpson"));
    }

    @Override
    public void shouldHaveDescriptiveRepresentation() {

        // given:
        ParticipantID participantId = new ParticipantID("homer.simpson");
        
        // when:
        String representation = participantId.toString();
        
        // then:
        assertThat(representation).isEqualTo("homer.simpson");
    }
    
}
