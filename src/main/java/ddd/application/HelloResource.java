package ddd.application;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Stateless
@Path("/hello")
public class HelloResource {

    @GET
    @Produces("text/plain")
    public String greetings(@QueryParam("name") String name){
        return "Hello, " + name + "!";
    }
    
}
