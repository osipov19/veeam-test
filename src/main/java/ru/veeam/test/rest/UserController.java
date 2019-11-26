package ru.veeam.test.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import ru.veeam.test.dto.UserDto;
import ru.veeam.test.exception.EntityNotFoundException;
import ru.veeam.test.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/v1/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll()
            .stream()
            .map(UserDto::builder)
            .map(UserDto.Builder::withUsername)
            .map(UserDto.Builder::withFirstName)
            .map(UserDto.Builder::withLastName)
            .map(UserDto.Builder::build)
            .collect(Collectors.toList());
    }

    @GetMapping("/{username}")
    public UserDto get(@PathVariable("username") String username) {
        return userService.findByUsername(username)
            .map(UserDto::builder)
            .map(UserDto.Builder::withAll)
            .map(UserDto.Builder::build)
            .orElseThrow(() -> new EntityNotFoundException(String.format("User %s not found", username)));
    }

    @PutMapping("/{username}")
    public UserDto update(@PathVariable("username") String username, @RequestBody @Valid UserDto input, Principal principal) {
        String name = principal.getName();
        if (!username.equals(principal.getName())) {
            throw new AccessDeniedException("Access denied");
        }
        return userService.updateUser(username, input)
            .map(UserDto::builder)
            .map(UserDto.Builder::withUsername)
            .map(UserDto.Builder::build)
            .orElseThrow(() -> new EntityNotFoundException(String.format("User %s not found", username)));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{username}")
    public void delete(@PathVariable("username") String username, Principal principal) {
        if (!username.equals(principal.getName())) {
            throw new AccessDeniedException("Access denied");
        }
        userService.deleteUser(username);
    }
}
