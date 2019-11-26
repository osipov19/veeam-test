package ru.veeam.test.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.veeam.test.model.Post;
import ru.veeam.test.model.User;

import javax.persistence.criteria.Join;

public class PostSpec {
    public static Specification<Post> hasAuthor(String username) {
        return (root, query, cb) -> {
            Join<Post, User> authorJoin = root.join("author");
            return cb.equal(authorJoin.get("username"), username);
        };
    }
    public static Specification<Post> hasId(String id) {
        return (root, query, cb) -> cb.equal(root.get("id"), id);
    }
}
