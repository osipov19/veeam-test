package ru.veeam.test.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.veeam.test.dto.UserDto;
import ru.veeam.test.model.User;
import ru.veeam.test.repository.UserRepository;
import ru.veeam.test.repository.specifications.UserSpec;
import ru.veeam.test.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> createUser(UserDto userDto) {
        return Optional.ofNullable(userDto)
            .map(dto -> merge(dto, dto.toUser()))
            .map(userRepository::save);
    }

    @Override
    public Optional<User> updateUser(String username, UserDto user) {
        return findByUsername(username)
                .map(u -> merge(user, u))
                .map(userRepository::save);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findOne(where(UserSpec.hasUsername(username)));
    }

    @Override
    public void deleteUser(String username) {
        findByUsername(username).ifPresent(userRepository::delete);
    }

    private User merge(UserDto input, User output) {
        Optional<UserDto> in = Optional.ofNullable(input);
        in.map(UserDto::getFirstName).ifPresent(output::setFirstName);
        in.map(UserDto::getLastName).ifPresent(output::setLastName);
        in.map(UserDto::getEmail).ifPresent(output::setEmail);
        in.map(UserDto::getPassword).map(passwordEncoder::encode).ifPresent(output::setPassword);
        return output;
    }
}
