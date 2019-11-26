package ru.veeam.test.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.veeam.test.dto.PostDto;
import ru.veeam.test.model.Post;
import ru.veeam.test.repository.PostRepository;
import ru.veeam.test.repository.specifications.PostSpec;
import ru.veeam.test.service.PostService;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Optional<Post> createPost(PostDto postDto) {
        return Optional.ofNullable(postDto)
            .map(dto -> merge(dto, dto.toPost()))
            .map(postRepository::save);
    }

    @Override
    public Optional<Post> updatePost(String id, PostDto postDto) {
        return findById(id)
            .map(post -> merge(postDto, post))
            .map(postRepository::save);
    }

    @Override
    public List<Post> getAll() {
        return postRepository.findAll();
    }

    @Override
    public Optional<Post> findById(String id) {
        return postRepository.findOne(where(PostSpec.hasId(id)));
    }

    @Override
    public void deletePost(String id) {
        findById(id).ifPresent(postRepository::delete);
    }

    private Post merge(PostDto input, Post output) {
        Optional<PostDto> in = Optional.ofNullable(input);
        in.map(PostDto::getContent).ifPresent(output::setContent);
        in.map(PostDto::getTitle).ifPresent(output::setTitle);
        return output;
    }
}
