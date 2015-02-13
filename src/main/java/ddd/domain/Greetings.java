package ddd.domain;

import javax.ejb.Stateless;

@Stateless
public class Greetings {

    public String greet(String name){
        
        return "Hello, " + name + "!";
    }
    
}
