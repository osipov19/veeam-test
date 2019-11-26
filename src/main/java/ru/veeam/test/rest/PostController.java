package ru.veeam.test.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import ru.veeam.test.dto.PostDto;
import ru.veeam.test.exception.EntityNotFoundException;
import ru.veeam.test.model.Post;
import ru.veeam.test.model.User;
import ru.veeam.test.service.PostService;
import ru.veeam.test.service.UserService;
import sun.plugin.dom.exception.InvalidStateException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/v1/posts")
public class PostController {

    private final UserService userService;
    private final PostService postService;

    @Autowired
    public PostController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping
    public List<PostDto> getAll() {
        return postService.getAll()
            .stream()
            .map(PostDto::builder)
            .map(PostDto.Builder::withId)
            .map(PostDto.Builder::withTitle)
            .map(PostDto.Builder::withContent)
            .map(PostDto.Builder::withAuthor)
            .map(PostDto.Builder::build)
            .collect(Collectors.toList());
    }

    @PostMapping
    public PostDto create(@RequestBody @Valid PostDto input, Principal principal) {
        return postService.createPost(input)
            .map(PostDto::builder)
            .map(PostDto.Builder::withAll)
            .map(PostDto.Builder::build)
            .orElseThrow(() -> new InvalidStateException("Error during post creation"));
    }

    @GetMapping("/{id}")
    public PostDto get(@PathVariable("id") String id) {
        return postService.findById(id)
            .map(PostDto::builder)
            .map(PostDto.Builder::withAll)
            .map(PostDto.Builder::build)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Post %s not found", id)));
    }

    @PutMapping("/{id}")
    public PostDto update(@PathVariable("id") String id, @RequestBody @Valid PostDto input, Principal principal) {
        if (!postService.findById(id).map(Post::getAuthor).map(User::getUsername).map(principal.getName()::equals)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Post %s not found", id)))) {
            throw new AccessDeniedException("Access denied");
        }
        return postService.updatePost(id, input)
            .map(PostDto::builder)
            .map(PostDto.Builder::withAll)
            .map(PostDto.Builder::build)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Post %s not found", id)));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id, Principal principal) {
        String name = principal.getName();
        if (!postService.findById(id).map(Post::getAuthor).map(User::getUsername).map(name::equals)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Post %s not found", id)))) {
            throw new AccessDeniedException("Access denied");
        }
        postService.deletePost(id);
    }
}
