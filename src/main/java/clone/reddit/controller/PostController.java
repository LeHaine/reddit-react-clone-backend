package clone.reddit.controller;

import clone.reddit.entity.Account;
import clone.reddit.entity.Post;
import clone.reddit.repository.AccountRepository;
import clone.reddit.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by colt on 7/8/18.
 */
@RestController
public class PostController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/post")
    public Page<Post> getAllThreads(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @GetMapping("/post/{postId}")
    public Post getPostById(@PathVariable Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }

    @PostMapping("/post")
    public Post createPost(@Valid @RequestBody Post post) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountRepository.findUserByUsernameAndPassword(userDetails.getUsername(), userDetails.getPassword());
        post.setAccount(account);
        return postRepository.save(post);
    }

    @PutMapping("/post/{postId}")
    public Post updatePost(@PathVariable Long postId, @Valid @RequestBody Post postRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountRepository.findUserByUsernameAndPassword(userDetails.getUsername(), userDetails.getPassword());
        return postRepository.findById(postId).map(post -> {
            if(postRequest.getAccount().equals(account)) {
                post.setContent(postRequest.getContent());
                return postRepository.save(post);
            }
            return post;
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountRepository.findUserByUsernameAndPassword(userDetails.getUsername(), userDetails.getPassword());
        return postRepository.findById(postId).map(post -> {
            if(post.getAccount().equals(account)) {
                postRepository.delete(post);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }
}
