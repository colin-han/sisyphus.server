package info.colinhan.sisyphus.server.repository;

import info.colinhan.sisyphus.server.model.FormEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FormRepository extends JpaRepository<FormEntity, Long> {
    Optional<FormEntity> findFirstByName(String name);
}
