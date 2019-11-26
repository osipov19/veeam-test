package ru.veeam.test.service;

import ru.veeam.test.dto.PostDto;
import ru.veeam.test.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Optional<Post> createPost(PostDto postDto);
    Optional<Post> updatePost(String id, PostDto postDto);
    List<Post> getAll();
    Optional<Post> findById(String id);
    void deletePost(String id);
}
