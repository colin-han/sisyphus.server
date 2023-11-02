package info.colinhan.sisyphus.server.repository;

import info.colinhan.sisyphus.server.model.ProgramEntity;
import info.colinhan.sisyphus.server.utils.ProgramStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ProgramRepository extends JpaRepository<ProgramEntity, Long> {
    List<ProgramEntity> findAllByStatus(ProgramStatus programStatus);

    List<ProgramEntity> findAllByFlowVersionIdInOrderByUpdatedAtDesc(List<Long> flowVersionIds);
}
