package ddd.domain;

import static org.assertj.core.api.Assertions.assertThat;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public abstract class ValueObjectContractTest {

    protected abstract Object instance();
    protected abstract Object[] equalInstances();
    protected abstract Object[] nonEqualInstances();

    @Test
    @Parameters(method = "equalInstances")
    public void shouldBeEqualIfHasSameValues(Object equalInstance) throws Exception {
    
        // given:
        final Object some = instance();
    
        // when:
        boolean areEqual = some.equals(equalInstance);
    
        // then
        assertThat(areEqual).isTrue();
    }

    @Test
    @Parameters(method = "nonEqualInstances")
    public void shouldNotBeEqualIfDifferentValues(Object nonEqual) throws Exception {
        
        // given:
        final Object some = instance();
    
        // when:
        boolean result = some.equals(nonEqual);
    
        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldNotBeEqualIfDifferentClassPassed() throws Exception {
    
        // given:
        final Object some = instance();
        
        // when:
        boolean result = some.equals("somethingDifferent");
    
        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldNotBeEqualIfNullPassed() throws Exception {
        
        // given:
        final Object some = instance();
    
        // when:
        boolean result = some.equals(null);
    
        // then
        assertThat(result).isFalse();
    }

    @Test
    @Parameters(method = "equalInstances")
    public void shouldHaveSameHashCodesIfObjectsEquals(Object equalInstance) throws Exception {
    
        // given:
        final Object some = instance();
    
        // when:
        boolean areEqual = some.hashCode() == equalInstance.hashCode();
        
        // then
        assertThat(areEqual).isTrue();
    }
    
    @Test
    public abstract void shouldHaveDescriptiveRepresentation();

}