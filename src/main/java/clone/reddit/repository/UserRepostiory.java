package clone.reddit.repository;

import clone.reddit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by colt on 7/8/18.
 */
public interface UserRepostiory extends JpaRepository<User, Long>{

}
