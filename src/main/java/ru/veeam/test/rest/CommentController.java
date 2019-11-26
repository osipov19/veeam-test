package ru.veeam.test.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import ru.veeam.test.dto.CommentDto;
import ru.veeam.test.exception.EntityNotFoundException;
import ru.veeam.test.model.Comment;
import ru.veeam.test.model.User;
import ru.veeam.test.service.CommentService;
import ru.veeam.test.service.PostService;
import ru.veeam.test.service.UserService;
import sun.plugin.dom.exception.InvalidStateException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/v1/posts/{post_id}/comments")
public class CommentController {

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    @Autowired
    public CommentController(UserService userService, PostService postService, CommentService commentService) {
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping
    public List<CommentDto> getAll(@PathVariable("post_id") String postId) {
        return commentService.findAllByPost(postId)
            .stream()
            .map(CommentDto::builder)
            .map(CommentDto.Builder::withId)
            .map(CommentDto.Builder::withContent)
            .map(CommentDto.Builder::withAuthor)
            .map(CommentDto.Builder::build)
            .collect(Collectors.toList());
    }

    @PostMapping
    public CommentDto create(@PathVariable("post_id") String postId, @RequestBody @Valid CommentDto input, Principal principal) {
        return commentService.createComment(postId, input)
            .map(CommentDto::builder)
            .map(CommentDto.Builder::withId)
            .map(CommentDto.Builder::build)
            .orElseThrow(() -> new InvalidStateException("Error during comment creation"));
    }

    @GetMapping("/{id}")
    public CommentDto get(@PathVariable("id") String id) {
        return commentService.findById(id)
            .map(CommentDto::builder)
            .map(CommentDto.Builder::withAll)
            .map(CommentDto.Builder::build)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Comment %s not found", id)));
    }

    @PutMapping("/{id}")
    public CommentDto update(@PathVariable("id") String id, @RequestBody @Valid CommentDto input, Principal principal) {
        if (!commentService.findById(id).map(Comment::getAuthor).map(User::getUsername).map(principal.getName()::equals)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Comment %s not found", id)))) {
            throw new AccessDeniedException("Access denied");
        }
        return commentService.updateComment(id, input)
            .map(CommentDto::builder)
            .map(CommentDto.Builder::withId)
            .map(CommentDto.Builder::build)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Comment %s not found", id)));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id, Principal principal) {
        if (!commentService.findById(id).map(Comment::getAuthor).map(User::getUsername).map(principal.getName()::equals)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Post %s not found", id)))) {
            throw new AccessDeniedException("Access denied");
        }
        commentService.deleteComment(id);
    }
}
