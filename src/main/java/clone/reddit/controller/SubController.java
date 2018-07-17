package clone.reddit.controller;

import clone.reddit.entity.Account;
import clone.reddit.entity.Sub;
import clone.reddit.repository.AccountRepository;
import clone.reddit.repository.SubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by colt on 7/9/18.
 */
@RestController
public class SubController {

    @Autowired
    private SubRepository subRepository;


    @GetMapping("/sub")
    public Page<Sub> getAllSubs(Pageable pageable) {
        return subRepository.findAll(pageable);
    }


    @GetMapping("/sub/{name}")
    public Sub getSubByName(@PathVariable String name) {
        return subRepository.findByName(name);
    }

    @PostMapping("/sub")
    public Sub createSub(@Valid @RequestBody Sub sub) {
        return subRepository.save(sub);
    }
}
