package ddd.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Test;

public class JpaFooRepositoryTest extends DatabaseIntegrationTest {

    private JpaFooRepository repository;
    
    @Before
    public void setUp() throws NamingException {
        repository = lookup(JpaFooRepository.class);
    }

    @Test
    public void shouldFooBar() throws Exception {

        // given:
        Foo foo = new Foo("bar");
        
        // when:
        repository.store(foo);
        
        flushAndClear();
        
        // then:
        assertThat(repository.load("bar")).isNotNull();
    }
    
}
