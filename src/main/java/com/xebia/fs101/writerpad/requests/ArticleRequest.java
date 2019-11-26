package com.xebia.fs101.writerpad.requests;

import com.xebia.fs101.writerpad.domain.Article;


import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class ArticleRequest {
    @NotBlank(message = "Please Provide Title")
    private String title;
    @NotBlank(message = "Please Provide description")
    private String description;
    @NotBlank(message = "Please Provide body")
    private String body;

    private Set<String> tags;

    private String featuredImageUrl;

    private ArticleRequest(Builder builder) {
        title = builder.title;
        description = builder.description;
        body = builder.body;
        tags = builder.tags;
        featuredImageUrl = builder.featuredImageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getBody() {
        return body;
    }

    public Set<String> getTags() {
        return tags == null ? Collections.emptySet() : tags;
    }

    public String getFeaturedImageUrl() {
        return featuredImageUrl;
    }

    public ArticleRequest() {
    }

    public Article toArticle() {
        return new Article.Builder().withTitle(this.title)
                .withDescription(this.description)
                .withBody(this.body)
                .withTags(this.getTags().stream().map(String::toLowerCase)
                        .collect(Collectors.toSet()))
                .withFeaturedImage(this.featuredImageUrl)
                .build();

    }


    public static final class Builder {
        private String title;
        private String description;
        private String body;
        private Set<String> tags;
        private String featuredImageUrl;

        public Builder() {
        }

        public Builder withTitle(String val) {
            title = val;
            return this;
        }

        public Builder withDescription(String val) {
            description = val;
            return this;
        }

        public Builder withBody(String val) {
            body = val;
            return this;
        }

        public Builder withTags(Set<String> val) {
            tags = val;
            return this;
        }

        public Builder withFeaturedImage(String val) {
            featuredImageUrl = val;
            return this;
        }

        public ArticleRequest build() {
            return new ArticleRequest(this);
        }
    }

    @Override
    public String toString() {
        return "ArticleRequest{"
                + "title='" + title + '\''
                + ", description='" + description + '\''
                + ", body='" + body + '\''
                + ", tags=" + tags
                + ", featuredImageUrl='" + featuredImageUrl
                + '\''
                + '}';
    }
}

