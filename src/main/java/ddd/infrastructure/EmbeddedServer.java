package ddd.infrastructure;

import ddd.application.HelloResource;

public class EmbeddedServer {

    public static void main(String args[]) {
        TomEEApplication.run(HelloResource.class);
    }

}