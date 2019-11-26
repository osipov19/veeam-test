package ru.veeam.test.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.veeam.test.model.Comment;
import ru.veeam.test.model.Post;

import javax.persistence.criteria.Join;

public class CommentSpec {
    public static Specification<Comment> hasPost(String postId) {
        return (root, query, cb) -> {
            Join<Comment, Post> postJoin = root.join("post");
            return cb.equal(postJoin.get("id"), postId);
        };
    }
    public static Specification<Comment> hasId(String id) {
        return (root, query, cb) -> cb.equal(root.get("id"), id);
    }
}
