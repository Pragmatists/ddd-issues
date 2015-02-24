package ddd.domain;

import static junitparams.JUnitParamsRunner.$;
import static org.assertj.core.api.Assertions.assertThat;

public class ProjectVersionIsValueObject extends ValueObjectContractTest {

    @Override
    protected Object instance() {
        return ProductVersion.of("buggy-app 1.2.3");
    }

    @Override
    protected Object[] equalInstances() {
        return $(ProductVersion.of("buggy-app 1.2.3"));
    }

    @Override
    protected Object[] nonEqualInstances() {
        return $(ProductVersion.of("buggy-add 1.2.3"), ProductVersion.of("buggy-app 1.2.4"));
    }

    @Override
    public void shouldHaveDescriptiveRepresentation() {

        // given:
        ProductVersion version = ProductVersion.of("buggy-app 1.2.3");
        
        // when:
        String representation = version.toString();
        
        // then:
        assertThat(representation).isEqualTo("buggy-app 1.2.3");
    }
    
}
