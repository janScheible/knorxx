package knorxx.framework.generator.springsampleapp.server.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author sj
 */
@Entity
public class TestEntity implements Serializable {
    
    private static final long serialVersionUID = 42L;
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)    
    private Long id;
    
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
