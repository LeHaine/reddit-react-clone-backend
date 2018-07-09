package clone.reddit.controller;

import clone.reddit.entity.Post;
import clone.reddit.repository.ThreadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by colt on 7/8/18.
 */
@RestController
public class ThreadController {

    @Autowired
    private ThreadRepository threadRepository;

    @GetMapping("/threads")
    public Page<Post> getAllThreads(Pageable pageable) {
        return threadRepository.findAll(pageable);
    }

    @GetMapping("/threads/{threadId}")
    public Post getThreadById(@PathVariable Long threadId) {
        return threadRepository.findById(threadId).orElseThrow(() -> new ResourceNotFoundException("ThreadId " + threadId + " not found"));
    }

    @PostMapping("/threads")
    public Post createThread(@Valid @RequestBody Post post) {
        return threadRepository.save(post);
    }

    @PutMapping("/threads/{threadId}")
    public Post updatePost(@PathVariable Long threadId, @Valid @RequestBody Post postRequest) {
        return threadRepository.findById(threadId).map(post -> {
            post.setContent(postRequest.getContent());
            return threadRepository.save(post);
        }).orElseThrow(() -> new ResourceNotFoundException("ThreadId " + threadId + " not found"));
    }

    @DeleteMapping("/threads/{threadId}")
    public ResponseEntity<?> deleteThread(@PathVariable Long threadId) {
        return threadRepository.findById(threadId).map(post -> {
            threadRepository.delete(post);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("ThreadId " + threadId + " not found"));
    }
}
