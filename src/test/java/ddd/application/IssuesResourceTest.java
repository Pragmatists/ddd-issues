package ddd.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import ddd.infrastructure.JpaFooRepository;

public class IssuesResourceTest extends EndToEndTest {

    JpaFooRepository repository;

    @Test
    public void shouldLookupBean() throws Exception {
        repository = lookup(JpaFooRepository.class);
        assertThat(repository).isNotNull();
    }

    @Test
    public void shouldLookupHelloResource() {
        assertThat(lookup(HelloResource.class)).isNotNull();

    }
}
