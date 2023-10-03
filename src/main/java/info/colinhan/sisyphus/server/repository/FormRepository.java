package info.colinhan.sisyphus.server.repository;

import info.colinhan.sisyphus.server.model.FormEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormRepository extends JpaRepository<FormEntity, Long> {
}
