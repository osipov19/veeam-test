package ru.veeam.test.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.veeam.test.model.Post;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostDto {

    private String id;
    @NotNull
    @Size(min = 1, max = 100)
    private String title;
    @NotNull
    private String content;
    private UserDto author;

    private List<CommentDto> comments;

    public static Builder builder(Post post) {
        return new Builder(post);
    }

    public static class Builder {
        private final Post post;
        private final PostDto dto;

        private Builder(Post post) {
            this.post = post;
            this.dto = new PostDto();
        }

        public PostDto build() {
            return dto;
        }

        public Builder withAll() {
            return this.withId()
                .withTitle()
                .withContent()
                .withAuthor()
                .withComments();
        }

        public Builder withId() {
            this.dto.setId(post.getId());
            return this;
        }

        public Builder withTitle() {
            this.dto.setTitle(post.getTitle());
            return this;
        }

        public Builder withContent() {
            this.dto.setContent(post.getContent());
            return this;
        }

        public Builder withAuthor() {
            this.dto.setAuthor(UserDto.builder(post.getAuthor()).withUsername().build());
            return this;
        }

        public Builder withComments() {
            this.dto.setComments(
                post.getComments().stream()
                    .map(CommentDto::builder)
                    .map(CommentDto.Builder::withId)
                    .map(CommentDto.Builder::withContent)
                    .map(CommentDto.Builder::withAuthor)
                    .map(CommentDto.Builder::build)
                    .collect(Collectors.toList())
            );
            return this;
        }
    }

    public Post toPost() {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setId(id);
        return post;
    }
}
