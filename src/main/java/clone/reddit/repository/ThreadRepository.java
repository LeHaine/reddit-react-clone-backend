package clone.reddit.repository;

import clone.reddit.entity.Thread;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by colt on 7/8/18.
 */
public interface ThreadRepository extends JpaRepository<Thread, Long> {
}
