package ru.veeam.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.veeam.test.model.User;
import ru.veeam.test.security.jwt.JwtUser;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditingConfiguration {
    @Bean
    public AuditorAware<User> auditorProvider() {
        return () -> {
            JwtUser principal = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return Optional.ofNullable(principal.getUser());
        };
    }
}
