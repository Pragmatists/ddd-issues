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
    private static int level = 0;
    
    @BeforeClass
    public static void startServer() {
        if(level == 0){
            application = TomEEApplication.application();
            application.start();
        }
        level++;
    }

    @BeforeClass
    public static void setupJsonSerialization() {
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(objectMapperConfig().jackson2ObjectMapperFactory(
                (aClass, s) -> objectMapper()
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
        level--;
        if(level == 0){
            application.stop();
            application.await();
        }
    }

}
