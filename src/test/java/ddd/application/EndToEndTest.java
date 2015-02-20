package ddd.application;

import static com.jayway.restassured.config.ObjectMapperConfig.*;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
                    @Override
                    public ObjectMapper create(Class aClass, String s) {
                        return new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                                .configure(SerializationFeature.WRAP_ROOT_VALUE, true);
                    }
                }
        ));
    }

    @Before
    public void injectFields() {
        BeanProvider.injectFields(this);
    }
    
    @AfterClass
    public static void stopServer() {
        application.stop();
    }

}
