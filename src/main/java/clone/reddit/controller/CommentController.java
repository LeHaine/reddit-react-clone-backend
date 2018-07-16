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
        return commentRepository.findAll(pageable).map(this::getCommentVoteInfo);
    }

    @GetMapping("/comment/{commentId}")
    public Comment getCommentById(@PathVariable String commentId) {
        return commentRepository.findById(commentId).map(this::getCommentVoteInfo).orElseThrow(() -> new ResourceNotFoundException("CommentId " + commentId + " not found"));
    }


    @GetMapping("/comment/post/{postId}")
    public Page<Comment> getAllCommentsByPostId(@PathVariable String postId, Pageable pageable) {
        return commentRepository.findByPostId(postId, pageable).map(this::getCommentVoteInfo);
    }

    @GetMapping("/comment/post/toplevel/{postId}")
    public Page<Comment> getToplevelCommentsByPostId(@PathVariable String postId, Pageable pageable) {
        return commentRepository.findByPostIdAndParentIdIsNull(postId, pageable).map(this::getCommentVoteInfo);
    }

    private Comment getCommentVoteInfo(Comment comment) {
        long upvotes = voteRepository.countByFlagAndCommentId(1, comment.getId());
        long downvotes = voteRepository.countByFlagAndCommentId(-1, comment.getId());
        comment.setGrossVotes(upvotes - downvotes);
        boolean isAuthed = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        if (isAuthed) {
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Account account = accountRepository.findUserByUsername(username);
            Vote vote = voteRepository.findByCommentIdAndAccount(comment.getId(), account);
            int flag = 0;
            if (vote != null) {
                flag = vote.getFlag();
            }
            comment.setVoteFlag(flag);
        }
        return comment;
    }


    @PutMapping("/comment/{commentId}")
    public Comment updateComment(@PathVariable String commentId, @Valid @RequestBody Comment commentRequest) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountRepository.findUserByUsername(username);
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
        Account account = accountRepository.findUserByUsername(username);
        return commentRepository.findById(commentId).map(comment -> {
            if (comment.getAccount().equals(account)) {
                commentRepository.delete(comment);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }).orElseThrow(() -> new ResourceNotFoundException("CommentId " + commentId + " not found"));
    }

    @PostMapping("/comment")
    public Comment createComment(@Valid @RequestBody Comment commentRequest) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountRepository.findUserByUsername(username);
        commentRequest.setAccount(account);
        commentRequest.setPost(postRepository.findById(commentRequest.getPost().getId())
                .orElseThrow(() -> new ResourceNotFoundException("PostId " + commentRequest.getPost().getId() + " not found")));
        Vote vote = new Vote();
        vote.setFlag(1);
        Comment comment = commentRepository.save(commentRequest);
        vote.setComment(comment);
        vote.setAccount(account);
        voteRepository.save(vote);
        return comment;
    }
}
