package ddd.infrastructure;

import java.io.File;
import java.nio.file.Files;
import javax.naming.NamingException;

import org.apache.tomee.embedded.Configuration;
import org.apache.tomee.embedded.Container;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import ddd.application.HelloResource;
import ddd.domain.Greetings;

public class TomEEApplication {

    private Container container;

    private final WebArchive archive;

    private TomEEApplication(Class<?>... classes) {
        archive = ShrinkWrap.create(WebArchive.class);
        archive.addClasses(classes);
        archive.addAsWebInfResource("META-INF/beans.xml", "beans.xml");
    }

    public static TomEEApplication application() {
        return new TomEEApplication(HelloResource.class, Greetings.class);
    }

    public void start() {

        try {
            Configuration configuration = new Configuration();
            String tomeeDir = Files.createTempDirectory("apache-tomee").toFile().getAbsolutePath();
            configuration.setDir(tomeeDir);
            configuration.setHttpPort(8080);

            container = new Container();
            container.setup(configuration);

            final File app = new File(Files.createTempDirectory("app").toFile().getAbsolutePath());
            app.deleteOnExit();

            File target = new File(app, "app.war");
            archive.as(ZipExporter.class).exportTo(target, true);
            container.start();

            container.deploy("app", target);
            registerShutdownHook(container);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void await() {
        container.await();
    }

    public void stop() {
        if (container != null) {
            try {
                container.stop();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void registerShutdownHook(final Container container) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    if (container != null) {
                        container.stop();
                    }
                } catch (final Exception e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
    }

    public <T> T lookup(Class<T> clazz) {
        try {
            return (T) container.getAppContexts("app").getGlobalJndiContext().lookup("global/app/" + clazz.getSimpleName());
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}