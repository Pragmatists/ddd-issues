package ddd.infrastructure;

import ddd.application.HelloResource;
import ddd.domain.Greetings;

public class EmbeddedServer {

    public static void main(String args[]) {
        TomEEApplication.run(HelloResource.class, Greetings.class);
    }

}