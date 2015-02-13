package ddd.infrastructure;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Foo {

    @Id
    private String bar;
    
    public Foo() {
    }
    
    public Foo(String bar) {
        this.bar = bar;
    }
    
}
