package info.colinhan.sisyphus.server.repository;

import info.colinhan.sisyphus.server.model.FormVersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FormVersionRepository extends JpaRepository<FormVersionEntity, Long> {

    Optional<FormVersionEntity> findFirstByFormIdOrderByVersionDesc(long flowId);

    Optional<FormVersionEntity> findOneByFormIdAndVersion(long flowId, int version);

    @Query("select v from form_versions v where v.form.name = :formName order by v.version desc")
    Optional<FormVersionEntity> findFirstByFormNameOrderByVersionDesc(String formName);
}
