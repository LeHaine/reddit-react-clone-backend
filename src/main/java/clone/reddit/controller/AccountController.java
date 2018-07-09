package clone.reddit.controller;

import clone.reddit.entity.Account;
import clone.reddit.repository.UserRepostiory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by colt on 7/9/18.
 */
@RestController
public class UserController {

    @Autowired
    private UserRepostiory userRepostiory;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/users")
    public Account createUser(@RequestParam String username, @RequestParam String password) {
        Account account = userRepostiory.findUserByUsername(username);
        if(account != null) {
            return null;
        }
        String encodedPassword = passwordEncoder.encode(password);
        System.out.println("Pass: " + password + " encoded: " + encodedPassword);
        account = new Account();
        account.setUsername(username);
        account.setPassword(encodedPassword);

        return userRepostiory.save(account);
    }

}
