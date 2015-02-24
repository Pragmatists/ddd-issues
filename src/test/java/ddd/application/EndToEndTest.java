package ddd.application;

import static com.jayway.restassured.config.ObjectMapperConfig.*;
import static ddd.infrastructure.CustomJacksonProvider.*;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.mapper.factory.Jackson2ObjectMapperFactory;

import ddd.infrastructure.TomEEApplication;

public abstract class EndToEndTest {

    private static TomEEApplication application;

    @BeforeClass
    public static void startServer() {
        application = TomEEApplication.application();
        application.start();
    }

    @BeforeClass
    public static void setupJsonSerialization() {
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(objectMapperConfig().jackson2ObjectMapperFactory(
                new Jackson2ObjectMapperFactory() {
                    @SuppressWarnings("rawtypes")
                    @Override
                    public ObjectMapper create(Class aClass, String s) {
                        return objectMapper();
                    }
                }
        ));
    }

    @Before
    public void injectFields() {
        BeanProvider.injectFields(this);
    }

    protected void doInTransaction(Runnable operation) {
        TransactionalWrapper wrapper = BeanProvider.getContextualReference(TransactionalWrapper.class);
        wrapper.run(operation);
    }

    @Stateless
    public static class TransactionalWrapper {
        
        @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
        public void run(Runnable operation){
            operation.run();
        }
    }
    
    @AfterClass
    public static void stopServer() {
        application.stop();
    }

}
