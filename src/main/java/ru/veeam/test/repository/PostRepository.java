package ru.veeam.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import ru.veeam.test.model.Post;

public interface PostRepository extends JpaRepository<Post, String>, JpaSpecificationExecutor<Post> {
}
