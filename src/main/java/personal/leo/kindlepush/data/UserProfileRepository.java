package personal.leo.kindlepush.data;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserProfileRepository extends CrudRepository<UserProfile, Long> {

    Optional<UserProfile> findByUserId(Long userId);

}
