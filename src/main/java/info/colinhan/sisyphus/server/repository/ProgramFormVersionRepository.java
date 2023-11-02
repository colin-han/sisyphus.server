package info.colinhan.sisyphus.server.repository;

import info.colinhan.sisyphus.server.model.ProgramFormVersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgramFormVersionRepository extends JpaRepository<ProgramFormVersionEntity, Long> {
    List<ProgramFormVersionEntity> findAllByProgramId(Long programId);
}
