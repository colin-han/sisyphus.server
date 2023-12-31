package info.colinhan.sisyphus.server.repository;

import info.colinhan.sisyphus.server.model.FlowVersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface FlowVersionRepository extends JpaRepository<FlowVersionEntity, Long> {

    Optional<FlowVersionEntity> findFirstByFlowIdOrderByVersionDesc(long flowId);

    Optional<FlowVersionEntity> findOneByFlowIdAndVersion(long flowId, int version);

    Collection<FlowVersionEntity> findAllByFlowId(long flowId);
}
