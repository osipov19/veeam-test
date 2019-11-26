package ru.veeam.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import ru.veeam.test.model.User;


public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
}
