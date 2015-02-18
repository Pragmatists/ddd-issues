package ddd.application;

import static org.assertj.core.api.Assertions.*;

import javax.inject.Inject;

import org.junit.Test;

import ddd.domain.Greetings;

public class IssuesResourceTest extends EndToEndTest {

    @Inject
    private Greetings greetings;
    
    @Inject
    private HelloResource helloResource;
    
    @Test
    public void shouldLookupGreetings() throws Exception {
        assertThat(greetings.greet("World")).isEqualTo("Hello, World!");
    }

    @Test
    public void shouldLookupHelloResource() {
        assertThat(helloResource.greetings("World")).isEqualTo("Hello, World!");

    }
}
