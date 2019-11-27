package com.xebia.fs101.writerpad.api.rest.representations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.domain.Comment;

import javax.validation.constraints.NotBlank;

public class CommentRequest {

    @NotBlank(message = "Please Provide body")
    private String body;

    @JsonCreator
    public CommentRequest(@NotBlank(message = "Please Provide body") String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public Comment toComment(String ipAddress, Article article) {
        return new Comment(this.body, ipAddress, article);
    }
}
