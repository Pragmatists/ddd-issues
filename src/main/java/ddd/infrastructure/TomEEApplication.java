package ddd.infrastructure;

import java.io.File;
import java.nio.file.Files;

import org.apache.tomee.embedded.Configuration;
import org.apache.tomee.embedded.Container;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class TomEEApplication {

    private static void startAndDeploy(Archive<?> archive) {

        Container container;

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
            container.await();

        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        registerShutdownHook(container);

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

    public static void run(Class<?>... clazzes) {
        run(ShrinkWrap.create(WebArchive.class).addClasses(clazzes));
    }

    public static void run(WebArchive archive) {
        startAndDeploy(archive);
    }
}