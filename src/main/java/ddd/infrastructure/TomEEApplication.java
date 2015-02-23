package ddd.infrastructure;

import java.io.File;
import java.nio.file.Files;

import org.apache.tomee.embedded.Configuration;
import org.apache.tomee.embedded.Container;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class TomEEApplication {

    private Container container;

    private final WebArchive archive;

    private TomEEApplication() {
        archive = ShrinkWrap.create(WebArchive.class);
        archive.addPackages(true, "ddd");
        archive.addAsWebInfResource("beans.xml", "beans.xml");
        archive.addAsWebResource("static/index.html", "index.html");
        archive.addAsManifestResource("META-INF/persistence.xml", "persistence.xml");
    }

    public static TomEEApplication application() {
        return new TomEEApplication();
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

}