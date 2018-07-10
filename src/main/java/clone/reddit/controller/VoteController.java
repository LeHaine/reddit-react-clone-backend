package clone.reddit.controller;

import clone.reddit.entity.Account;
import clone.reddit.entity.Vote;
import clone.reddit.repository.AccountRepository;
import clone.reddit.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by colt on 7/8/18.
 */
@RestController
public class VoteController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private VoteRepository voteRepository;


    @PostMapping("/vote")
    public Vote createVote(@Valid @RequestBody Vote vote) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountRepository.findUserByUsername(username);
        vote.setAccount(account);
        return voteRepository.save(vote);
    }

    @PutMapping("/vote/{voteId}")
    public Vote updateVote(@PathVariable Long voteId, @Valid @RequestBody Vote voteRequest) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountRepository.findUserByUsername(username);
        return voteRepository.findById(voteId).map(vote -> {
            if(vote.getAccount().equals(account)) {
                vote.setFlag(voteRequest.getFlag());
                return voteRepository.save(vote);
            }
            return vote;
        }).orElseThrow(() -> new ResourceNotFoundException("VoteId " + voteId + " not found"));
    }
}
