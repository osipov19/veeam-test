package ru.veeam.test.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.veeam.test.model.User;

public class UserSpec {
    public static Specification<User> hasUsername(String username) {
        return (root, query, cb) -> cb.equal(root.get("username"), username);
    }
}
