package ru.veeam.test.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.veeam.test.dto.UserDto;
import ru.veeam.test.exception.EntityNotFoundException;
import ru.veeam.test.model.User;
import ru.veeam.test.security.jwt.JwtTokenProvider;
import ru.veeam.test.service.UserService;
import sun.plugin.dom.exception.InvalidStateException;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/v1/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserDto input) {
        String username = input.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, input.getPassword()));

        User user = userService.findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException(String.format("User %s not found", username)));
        String token = tokenProvider.createToken(username, user.getRole());

        return ResponseEntity.ok(String.format("JWT%s", token));
    }

    @PostMapping("/register")
    public UserDto register(@RequestBody @Valid UserDto input) {
        if (input.getUsername() == null || input.getPassword() == null || input.getEmail() == null) {
            throw new BadCredentialsException("Not enough data for registration");
        }
        return userService.createUser(input)
            .map(UserDto::builder)
            .map(UserDto.Builder::withAll)
            .map(UserDto.Builder::build)
            .orElseThrow(() -> new InvalidStateException("Error during registration"));
    }
}
