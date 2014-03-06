package knorxx.framework.generator.javaeesampleapp.server;

import com.mysema.query.jpa.impl.JPADeleteClause;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import knorxx.framework.generator.javaeesampleapp.server.model.QTestEntity;
import knorxx.framework.generator.javaeesampleapp.server.model.TestEntity;

/**
 *
 * @author sj
 */
@Singleton
@Startup
public class InitializeDatabase {
    
    @PersistenceContext
    EntityManager entityManager;
    
    @PostConstruct
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void initializeDatabase() {
        new JPADeleteClause(entityManager, QTestEntity.testEntity).execute();
        
        TestEntity testEntity = new TestEntity();
        testEntity.setName("Darkwing Duck");
        entityManager.persist(testEntity);
    }
}
