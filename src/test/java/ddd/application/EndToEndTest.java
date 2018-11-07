package ddd.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.RestAssuredConfig;
import ddd.infrastructure.CustomJacksonProvider;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.jayway.restassured.config.ObjectMapperConfig.objectMapperConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class EndToEndTest {

    @Autowired
    private TransactionalWrapper wrapper;

    protected void doInTransaction(Runnable operation) {
        wrapper.run(operation);
    }

    @BeforeClass
    public static void setupJsonSerialization() {
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(objectMapperConfig().jackson2ObjectMapperFactory(
                (aClass, s) -> {
                    return new CustomJacksonProvider().objectMapper();
                }
        ));
    }


    @Component
    public static class TransactionalWrapper {

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        public void run(Runnable operation){
            operation.run();
        }
    }


}
