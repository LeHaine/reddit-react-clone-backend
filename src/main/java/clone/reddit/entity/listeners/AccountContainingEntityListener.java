package clone.reddit.entity.listeners;

import clone.reddit.entity.Account;
import clone.reddit.entity.AccountValidatorModel;
import clone.reddit.repository.AccountRepository;
import clone.reddit.util.BeanUtil;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.PrePersist;

/**
 * Created by colt on 7/17/18.
 */
public class AccountContainingEntityListener {

    @PrePersist
    public void onPreUpdateOrPersist(AccountValidatorModel entity) {
        AccountRepository accountRepository = BeanUtil.getBean(AccountRepository.class);
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Account account = accountRepository.findByUsername(username);
                if (account != null) {
                    entity.setAccount(account);
                }
            }

        }
    }
}
