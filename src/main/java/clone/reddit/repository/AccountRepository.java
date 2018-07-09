package clone.reddit.repository;

import clone.reddit.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by colt on 7/8/18.
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findUserByUsername(String username);
    Account findUserByUsernameAndPassword(String username, String password);
}
