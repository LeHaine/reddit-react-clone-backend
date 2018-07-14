package clone.reddit.repository;

import clone.reddit.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by colt on 7/8/18.
 */
public interface CommentRepository extends JpaRepository<Comment, String> {

    Page<Comment> findByPostId(String postId, Pageable pageable);
    long countByPostId(String postId);
}
