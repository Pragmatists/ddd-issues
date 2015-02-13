package ddd.application;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import ddd.domain.Greetings;

@Stateless
@Path("/hello")
public class HelloResource {

    @Inject
    private Greetings greetings;
    
    @GET
    @Produces("text/plain")
    public String greetings(@QueryParam("name") String name){
        return greetings.greet(name);
    }
    
}
