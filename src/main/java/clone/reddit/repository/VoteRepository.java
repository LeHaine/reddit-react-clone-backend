package clone.reddit.repository;

import clone.reddit.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by colt on 7/8/18.
 */
public interface VoteRepository extends JpaRepository<Vote, Long> {

}
