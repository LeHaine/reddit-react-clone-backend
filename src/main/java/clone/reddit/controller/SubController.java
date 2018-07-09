package clone.reddit.controller;

import clone.reddit.entity.Account;
import clone.reddit.entity.Sub;
import clone.reddit.repository.AccountRepository;
import clone.reddit.repository.SubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Created by colt on 7/9/18.
 */
@RestController
public class SubController {


    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private SubRepository subRepository;

    @GetMapping("/sub/{name}")
    public Sub getSubByName(@PathVariable String name) {
        return subRepository.findByName(name);
    }

    @PostMapping("/sub")
    public Sub createSub(@RequestParam String name) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountRepository.findUserByUsernameAndPassword(userDetails.getUsername(), userDetails.getPassword());
        if(account != null) {
            Sub sub = new Sub();
            sub.setName(name);
            sub.setOwner(account);
            return subRepository.save(sub);
        }

        //TODO implement error handling
        return null;
    }
}
