package ddd.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import ddd.domain.Greetings;

public class IssuesResourceTest extends EndToEndTest {

    @Test
    public void shouldLookupGreetings() throws Exception {
        assertThat(lookup(Greetings.class)).isNotNull();
    }

    @Test
    public void shouldLookupHelloResource() {
        assertThat(lookup(HelloResource.class)).isNotNull();

    }
}
