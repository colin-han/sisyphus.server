package info.colinhan.sisyphus.server.repository;

import info.colinhan.sisyphus.server.model.FlowEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlowRepository extends JpaRepository<FlowEntity, Long> {
}