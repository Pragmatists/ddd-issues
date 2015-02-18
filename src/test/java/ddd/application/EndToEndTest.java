package ddd.application;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import ddd.infrastructure.TomEEApplication;

public abstract class EndToEndTest {

    private static TomEEApplication application;

    @BeforeClass
    public static void startServer() {
        application = TomEEApplication.application();
        application.start();
    }

    @Before
    public void injectFields() {
        BeanProvider.injectFields(this);
    }
    
    @AfterClass
    public static void stopServer() {
        application.stop();
    }

    protected <T> T lookup(Class<T> clazz) {
        return BeanProvider.getContextualReference(clazz);
    }
}
