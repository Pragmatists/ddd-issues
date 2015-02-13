package ddd.infrastructure;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class JpaFooRepository {

    @PersistenceContext(unitName="issues-unit")
    private EntityManager entityManager;
    
    public void store(Foo foo){
        entityManager.persist(foo);
    }
    
    public Foo load(String bar){
        return entityManager.find(Foo.class, bar);
    }
    
    
}
