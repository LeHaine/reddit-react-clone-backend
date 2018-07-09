package clone.reddit.repository;

import clone.reddit.entity.Sub;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by colt on 7/9/18.
 */
public interface SubRepository extends JpaRepository<Sub, Long> {
    Sub findByName(String name);
}
