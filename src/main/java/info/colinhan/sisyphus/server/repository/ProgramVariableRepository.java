package info.colinhan.sisyphus.server.repository;

import info.colinhan.sisyphus.server.model.ProgramVariableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramVariableRepository extends JpaRepository<ProgramVariableEntity, Long> {
}
