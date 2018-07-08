package clone.reddit.controller;

import clone.reddit.entity.Thread;
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
    ThreadRepository threadRepository;

    @GetMapping("/threads")
    public Page<Thread> getAllThreads(Pageable pageable) {
        return threadRepository.findAll(pageable);
    }

    @PostMapping("/threads")
    public Thread createThread(@Valid @RequestBody Thread thread) {
        return threadRepository.save(thread);
    }

    @PutMapping("/threads/{threadId}")
    public Thread updatePost(@PathVariable Long threadId, @Valid @RequestBody Thread threadRequest) {
        return threadRepository.findById(threadId).map(thread -> {
            thread.setContent(threadRequest.getContent());
            return threadRepository.save(thread);
        }).orElseThrow(() -> new ResourceNotFoundException("ThreadId " + threadId + " not found"));
    }


    @PutMapping("/threads/upvote/{threadId}")
    public Thread upvotePost(@PathVariable Long threadId, @Valid @RequestBody Thread threadRequest) {
        return threadRepository.findById(threadId).map(thread -> {
            thread.setUpvotes(thread.getUpvotes() + 1);
            return threadRepository.save(thread);
        }).orElseThrow(() -> new ResourceNotFoundException("ThreadId " + threadId + " not found"));
    }

    @PutMapping("/threads/downvote/{threadId}")
    public Thread downvotePost(@PathVariable Long threadId, @Valid @RequestBody Thread threadRequest) {
        return threadRepository.findById(threadId).map(thread -> {
            thread.setUpvotes(thread.getDownvotes() + 1);
            return threadRepository.save(thread);
        }).orElseThrow(() -> new ResourceNotFoundException("ThreadId " + threadId + " not found"));
    }

    @DeleteMapping("/threads/{threadId}")
    public ResponseEntity<?> deleteThread(@PathVariable Long threadId) {
        return threadRepository.findById(threadId).map(thread -> {
            threadRepository.delete(thread);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("ThreadId " + threadId + " not found"));
    }
}
