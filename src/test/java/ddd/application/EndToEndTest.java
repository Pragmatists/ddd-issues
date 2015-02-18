package ddd.application;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import ddd.infrastructure.TomEEApplication;

public abstract class EndToEndTest {

    private static TomEEApplication application;

    @BeforeClass
    public static void startServer() {
        application = TomEEApplication.application();
        application.start();
    }

    @AfterClass
    public static void stopServer() {
        application.stop();
    }

    protected <T> T lookup(Class<T> clazz) {
        return application.lookup(clazz);
    }
}
