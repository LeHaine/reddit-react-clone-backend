package clone.reddit.controller;

import clone.reddit.entity.Account;
import clone.reddit.entity.Post;
import clone.reddit.entity.Sub;
import clone.reddit.entity.Vote;
import clone.reddit.repository.AccountRepository;
import clone.reddit.repository.PostRepository;
import clone.reddit.repository.SubRepository;
import clone.reddit.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;

/**
 * Created by colt on 7/8/18.
 */
@RestController
public class PostController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private SubRepository subRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private VoteRepository voteRepository;

    @GetMapping("/post")
    public Page<Post> getAllThreads(Pageable pageable) {
        return postRepository.findAll(pageable).map(this::getPostVoteInfo);
    }

    @GetMapping("/post/{postId}")
    public Post getPostById(@PathVariable String postId) {
        return postRepository.findById(postId).map(this::getPostVoteInfo).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }

    private Post getPostVoteInfo(Post post) {
        long upvotes = voteRepository.countByFlagAndPost(1, post);
        long downvotes = voteRepository.countByFlagAndPost(-1, post);
        post.setGrossVotes(Math.max(upvotes - downvotes, 0));
        boolean isAuthed = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        if(isAuthed) {
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Account account = accountRepository.findUserByUsername(username);
            Vote vote = voteRepository.findByPostAndAccount(post, account);
            int flag = 0;
            if(vote != null) {
                flag = vote.getFlag();
            }
            post.setVoteFlag(flag);
        }
        return post;
    }

    @PostMapping("/post")
    public Post createPost(@Valid @RequestBody Post post) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountRepository.findUserByUsername(username);
        Sub sub = subRepository.findByName(post.getSub().getName());
        post.setSub(sub);
        post.setAccount(account);
        Vote vote = new Vote();
        vote.setFlag(1);
        Post newPost = postRepository.save(post);
        vote.setPost(newPost);
        vote.setAccount(account);
        voteRepository.save(vote);
        return newPost;
    }

    @PutMapping("/post/{postId}")
    public Post updatePost(@PathVariable String postId, @Valid @RequestBody Post postRequest) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountRepository.findUserByUsername(username);
        return postRepository.findById(postId).map(post -> {
            if (postRequest.getAccount().equals(account)) {
                post.setContent(postRequest.getContent());
                return postRepository.save(post);
            }
            return post;
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable String postId) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountRepository.findUserByUsername(username);
        return postRepository.findById(postId).map(post -> {
            if (post.getAccount().equals(account)) {
                postRepository.delete(post);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }
}
