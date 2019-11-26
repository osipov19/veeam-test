package ru.veeam.test.service;

import java.util.List;
import java.util.Optional;

import ru.veeam.test.dto.CommentDto;
import ru.veeam.test.model.Comment;

public interface CommentService {
    Optional<Comment> createComment(String postId, CommentDto commentDto);
    Optional<Comment> updateComment(String id, CommentDto commentDto);
    List<Comment> findAllByPost(String post);
    Optional<Comment> findById(String id);
    void deleteComment(String id);
}
