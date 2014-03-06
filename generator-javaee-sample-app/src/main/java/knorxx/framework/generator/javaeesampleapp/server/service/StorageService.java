package knorxx.framework.generator.javaeesampleapp.server.service;

import com.mysema.query.jpa.impl.JPAQuery;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import knorxx.framework.generator.javaeesampleapp.server.model.QTestEntity;
import knorxx.framework.generator.javaeesampleapp.server.model.TestEntity;
import knorxx.framework.generator.web.client.RpcService;
import org.stjs.javascript.functions.Callback1;

/**
 *
 * @author sj
 */
public class StorageService implements RpcService {
    
    @PersistenceContext
    EntityManager entityManager;
    
    public TestEntity getById(HttpServletRequest request, long id, Callback1<TestEntity> callback, Object scope) {
        TestEntity testEntity = new JPAQuery (entityManager).from(QTestEntity.testEntity)
                .where(QTestEntity.testEntity.name.contains("wing"))
                .singleResult(QTestEntity.testEntity);
        return testEntity;        
    }
}
