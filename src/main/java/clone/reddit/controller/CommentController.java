package clone.reddit.controller;

import clone.reddit.entity.*;
import clone.reddit.repository.AccountRepository;
import clone.reddit.repository.CommentRepository;
import clone.reddit.repository.PostRepository;
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

/**
 * Created by colt on 7/14/18.
 */
@RestController
public class CommentController {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    PostRepository postRepository;


    @GetMapping("/comment")
    public Page<Comment> getAllThreads(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    @GetMapping("/comment/{commentId}")
    public Comment getCommentById(@PathVariable String commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("CommentId " + commentId + " not found"));
    }


    @GetMapping("/comment/post/{postId}")
    public Page<Comment> getAllCommentsByPostId(@PathVariable String postId, Pageable pageable) {
        return commentRepository.findByPostId(postId, pageable);
    }

    @GetMapping("/comment/post/toplevel/{postId}")
    public Page<Comment> getToplevelCommentsByPostId(@PathVariable String postId, Pageable pageable) {
        return commentRepository.findByPostIdAndParentIdIsNull(postId, pageable);
    }

    @PutMapping("/comment/{commentId}")
    public Comment updateComment(@PathVariable String commentId, @Valid @RequestBody Comment commentRequest) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountRepository.findByUsername(username);
        return commentRepository.findById(commentId).map(comment -> {
            if (commentRequest.getAccount().equals(account)) {
                comment.setText(commentRequest.getText());
                return commentRepository.save(comment);
            }
            return comment;
        }).orElseThrow(() -> new ResourceNotFoundException("CommentId " + commentId + " not found"));
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable String commentId) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountRepository.findByUsername(username);
        return commentRepository.findById(commentId).map(comment -> {
            if (comment.getAccount().equals(account)) {
                commentRepository.delete(comment);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }).orElseThrow(() -> new ResourceNotFoundException("CommentId " + commentId + " not found"));
    }

    @PostMapping("/comment")
    public Comment createComment(@RequestParam  String type, @Valid @RequestBody Comment commentRequest) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountRepository.findByUsername(username);
        if(type.equalsIgnoreCase("post")) {
            commentRequest.setPost(postRepository.findById(commentRequest.getPost().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("PostId " + commentRequest.getPost().getId() + " not found")));
        } else if (type.equalsIgnoreCase("comment")) {
            commentRequest.setParent(commentRepository.findById(commentRequest.getParent().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("CommentId " + commentRequest.getParent().getId() + " not found")));

            commentRequest.setPost(postRepository.findById(commentRequest.getParent().getPost().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("PostId " + commentRequest.getParent().getPost().getId() + " not found")));
        }
        Vote vote = new Vote();
        vote.setFlag(1);
        Comment comment = commentRepository.save(commentRequest);
        vote.setComment(comment);
        voteRepository.save(vote);
        return comment;
    }
}
