package start.openApi.openapi.xml;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public interface DbRepository extends JpaRepository<DB,Long> {


}
