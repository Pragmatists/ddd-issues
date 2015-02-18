package ddd.infrastructure;

public class EmbeddedServer {

    public static void main(String args[]) {
        TomEEApplication application = TomEEApplication.application();
        application.start();
        application.await();

    }

}