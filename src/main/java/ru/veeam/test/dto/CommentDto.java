package ru.veeam.test.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.veeam.test.model.Comment;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentDto {

    private String id;
    @NotNull
    private String content;
    private PostDto post;
    private UserDto author;

    public static Builder builder(Comment comment) {
        return new Builder(comment);
    }

    public static class Builder {
        private final Comment comment;
        private final CommentDto dto;


        private Builder(Comment comment) {
            this.comment = comment;
            this.dto = new CommentDto();
        }
        public CommentDto build() {
            return dto;
        }
        public Builder withAll() {
            return this.withId()
                .withContent()
                .withAuthor()
                .withPost();
        }
        public Builder withId() {
            this.dto.setId(comment.getId());
            return this;
        }
        public Builder withContent() {
            this.dto.setContent(comment.getContent());
            return this;
        }
        public Builder withAuthor() {
            this.dto.setAuthor(UserDto.builder(comment.getAuthor()).withId().withUsername().build());
            return this;
        }
        public Builder withPost() {
            this.dto.setPost(PostDto.builder(comment.getPost()).withId().withTitle().build());
            return this;
        }
    }

    public Comment toComment() {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setContent(content);
        return comment;
    }
}
