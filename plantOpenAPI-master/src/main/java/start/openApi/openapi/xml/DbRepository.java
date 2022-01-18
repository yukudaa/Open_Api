package start.openApi.openapi.xml;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class DbRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(DB db) {
        em.persist(db);
        return db.getId();
    }

    public DB find(int id) {
        return em.find(DB.class,id);
    }


}
