package ru.veeam.test.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.veeam.test.dto.CommentDto;
import ru.veeam.test.exception.EntityNotFoundException;
import ru.veeam.test.model.Comment;
import ru.veeam.test.model.Post;
import ru.veeam.test.repository.CommentRepository;
import ru.veeam.test.repository.specifications.CommentSpec;
import ru.veeam.test.service.CommentService;
import ru.veeam.test.service.PostService;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class CommentServiceImpl implements CommentService {

    private final PostService postService;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(PostService postService, CommentRepository commentRepository) {
        this.postService = postService;
        this.commentRepository = commentRepository;
    }

    @Override
    public Optional<Comment> createComment(String postId, CommentDto commentDto) {
        Post post = postService.findById(postId)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Post %s not found", postId)));
        return Optional.ofNullable(commentDto)
            .map(dto -> merge(dto, dto.toComment()))
            .map(comment -> comment.setPost(post))
            .map(commentRepository::save);
    }

    @Override
    public Optional<Comment> updateComment(String id, CommentDto commentDto) {
        return findById(id)
            .map(comment -> merge(commentDto, comment))
            .map(commentRepository::save);
    }

    @Override
    public List<Comment> findAllByPost(String postId) {
        return commentRepository.findAll(where(CommentSpec.hasPost(postId)));
    }

    @Override
    public Optional<Comment> findById(String id) {
        return commentRepository.findOne(where(CommentSpec.hasId(id)));
    }

    @Override
    public void deleteComment(String id) {
        findById(id).ifPresent(commentRepository::delete);
    }

    private Comment merge(CommentDto input, Comment output) {
        Optional<CommentDto> in = Optional.ofNullable(input);
        in.map(CommentDto::getContent).ifPresent(output::setContent);
        return output;
    }
}
