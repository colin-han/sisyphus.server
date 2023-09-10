package info.colinhan.sisyphus.server.repository;

import info.colinhan.sisyphus.server.model.FlowVersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlowVersionRepository extends JpaRepository<FlowVersionEntity, Long> {

    Optional<FlowVersionEntity> findLastByFlowIdOrderByVersion(long flowId);
}
