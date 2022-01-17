package start.openApi.openapi.xml;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DbRepository extends JpaRepository<DB,Long> {
}
