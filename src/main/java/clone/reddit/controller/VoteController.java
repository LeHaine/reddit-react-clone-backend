package clone.reddit.controller;

import clone.reddit.entity.Account;
import clone.reddit.entity.Comment;
import clone.reddit.entity.Post;
import clone.reddit.entity.Vote;
import clone.reddit.repository.AccountRepository;
import clone.reddit.repository.CommentRepository;
import clone.reddit.repository.PostRepository;
import clone.reddit.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by colt on 7/8/18.
 */
@RestController
public class VoteController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private CommentRepository commentRepository;

    @PostMapping("/vote/post")
    public Vote voteOnPost(@Valid @RequestBody Vote vote) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountRepository.findByUsername(username);
        Post post = postRepository.findById(vote.getPost().getId()).orElseThrow(() -> new ResourceNotFoundException("PostId " + vote.getPost().getId() + " does not exist"));
        Vote voteRequest = vote;
        Vote existingVote = voteRepository.findByPostIdAndAccount(post.getId(), account);
        if (existingVote != null) {
            voteRequest = existingVote;
        }
        if (vote.getFlag() > 0) {
            voteRequest.setFlag(1);
        } else if (vote.getFlag() < 0) {
            voteRequest.setFlag(-1);
        } else {
            voteRequest.setFlag(0);
        }
        return voteRepository.save(voteRequest);
    }

    @PostMapping("/vote/comment")
    public Vote voteOnComment(@Valid @RequestBody Vote vote) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountRepository.findByUsername(username);
        Comment comment = commentRepository.findById(vote.getComment().getId()).orElseThrow(() -> new ResourceNotFoundException("CommentId " + vote.getComment().getId() + " does not exist"));
        Vote voteRequest = vote;
        Vote existingVote = voteRepository.findByCommentIdAndAccount(comment.getId(), account);
        if (existingVote != null) {
            voteRequest = existingVote;
        }
        if (vote.getFlag() > 0) {
            voteRequest.setFlag(1);
        } else if (vote.getFlag() < 0) {
            voteRequest.setFlag(-1);
        } else {
            voteRequest.setFlag(0);
        }
        return voteRepository.save(voteRequest);
    }
}
