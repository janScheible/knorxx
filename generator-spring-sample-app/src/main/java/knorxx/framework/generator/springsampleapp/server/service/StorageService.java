package knorxx.framework.generator.springsampleapp.server.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import knorxx.framework.generator.springsampleapp.server.model.TestEntity;
import knorxx.framework.generator.web.client.RpcService;
import org.springframework.stereotype.Component;
import org.stjs.javascript.functions.Callback1;

/**
 *
 * @author sj
 */
@Component
public class StorageService implements RpcService {
    
    @PersistenceContext
    EntityManager entityManager;
    
    public TestEntity getById(HttpServletRequest request, long id, Callback1<TestEntity> callback, Object scope) {
        TestEntity testEntity = new TestEntity();
        testEntity.setName("Darkwing Duck");
        return testEntity;    
    }
}
