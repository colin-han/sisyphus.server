package info.colinhan.sisyphus.server.repository;

import info.colinhan.sisyphus.server.model.UserEntity;
import org.springframework.data.repository.Repository;

public interface UserRepository extends Repository<UserEntity, Long> {
}
