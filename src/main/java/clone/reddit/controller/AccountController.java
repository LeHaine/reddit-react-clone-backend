package clone.reddit.controller;

import clone.reddit.entity.Account;
import clone.reddit.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Created by colt on 7/9/18.
 */
@RestController
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/account")
    public Page<Account> getAllAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }

    @GetMapping("/account/{id}")
    public Account getAccountById(@PathVariable Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("AccountId " + id + " not found"));
    }

    @PostMapping("/account")
    public Account createAccount(@RequestParam String username, @RequestParam String password) {
        Account account = accountRepository.findUserByUsername(username);
        if(account != null) {
            //TODO implement error handling
            return null;
        }
        String encodedPassword = passwordEncoder.encode(password);
        account = new Account();
        account.setUsername(username);
        account.setPassword(encodedPassword);

        return accountRepository.save(account);
    }

}
