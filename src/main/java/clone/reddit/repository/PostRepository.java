package clone.reddit.repository;

import clone.reddit.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by colt on 7/8/18.
 */
public interface PostRepository extends JpaRepository<Post, String> {
}
