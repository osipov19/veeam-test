package ru.veeam.test.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.veeam.test.model.User;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private String id;
    @NotNull
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    private List<PostDto> posts;
    private List<CommentDto> comments;

    public static Builder builder(User user) {
        return new Builder(user);
    }

    public static class Builder {
        private final User user;
        private final UserDto dto;

        public Builder(User user) {
            this.user = user;
            this.dto = new UserDto();
        }

        public UserDto build() {
            return dto;
        }

        public Builder withAll() {
            return this
                .withId()
                .withUsername()
                .withEmail()
                .withFirstName()
                .withLastName()
                .withPosts()
                .withComments();
        }

        public Builder withId() {
            this.dto.setId(user.getId());
            return this;
        }

        public Builder withUsername() {
            this.dto.setUsername(user.getUsername());
            return this;
        }

        public Builder withEmail() {
            this.dto.setEmail(user.getEmail());
            return this;
        }

        public Builder withPassword() {
            this.dto.setPassword(user.getPassword());
            return this;
        }

        public Builder withFirstName() {
            this.dto.setFirstName(user.getFirstName());
            return this;
        }

        public Builder withLastName() {
            this.dto.setLastName(user.getLastName());
            return this;
        }

        public Builder withPosts() {
            this.dto.setPosts(
                user.getPosts().stream()
                    .map(PostDto::builder)
                    .map(PostDto.Builder::withId)
                    .map(PostDto.Builder::withTitle)
                    .map(PostDto.Builder::build)
                    .collect(Collectors.toList())
            );
            return this;
        }

        public Builder withComments() {
            this.dto.setComments(
                user.getComments().stream()
                    .map(CommentDto::builder)
                    .map(CommentDto.Builder::withId)
                    .map(CommentDto.Builder::withContent)
                    .map(CommentDto.Builder::withPost)
                    .map(CommentDto.Builder::build)
                    .collect(Collectors.toList())
            );
            return this;
        }
    }

    public User toUser() {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        return user;
    }
}
