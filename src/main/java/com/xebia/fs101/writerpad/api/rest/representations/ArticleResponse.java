package com.xebia.fs101.writerpad.api.rest.representations;

import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.domain.ArticleStatus;

import java.util.Date;
import java.util.Set;

public class ArticleResponse {
    private String id;
    private String title;
    private String slug;
    private String description;
    private String body;
    private Set<String> tags;
    private Date createdAt;
    private Date updatedAt;
    private boolean favourited;
    private long favouritesCount;
    private UserResponse author;
    private ArticleStatus status;
    private String featuredImageUrl;

    private ArticleResponse(Builder builder) {
        id = builder.id;
        title = builder.title;
        slug = builder.slug;
        description = builder.description;
        body = builder.body;
        tags = builder.tags;
        createdAt = builder.createdAt;
        updatedAt = builder.updatedAt;
        favourited = builder.favourited;
        favouritesCount = builder.favouritesCount;
        author = builder.author;
        status = builder.status;
        featuredImageUrl = builder.featuredImageUrl;
    }

    public static ArticleResponse from(Article article) {
        return new ArticleResponse.Builder()
                .withId(article.getId().toString())
                .withBody(article.getBody())
                .withAuthor(new UserResponse(article.getUser().getUsername()))
                .withCreatedAt(article.getCreatedAt())
                .withUpdatedAt(article.getUpdatedAt())
                .withDescription(article.getDescription())
                .withSlug(article.getSlug())
                .withTags(article.getTags())
                .withTitle(article.getTitle())
                .withFavouritesCount(article.getFavouritesCount())
                .withFavourited(article.isFavourited())
                .withStatus(article.getStatus())
                .withFeaturedImageUrl(article.getFeaturedImageUrl())
                .build();
    }

    private static class UserResponse {
        private String username;

        UserResponse(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSlug() {
        return slug;
    }

    public String getDescription() {
        return description;
    }

    public String getBody() {
        return body;
    }

    public Set<String> getTags() {
        return tags;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public boolean isFavourited() {
        return favourited;
    }

    public long getFavouritesCount() {
        return favouritesCount;
    }

    public UserResponse getAuthor() {
        return author;
    }

    public ArticleStatus getStatus() {
        return status;
    }

    public String getFeaturedImageUrl() {
        return featuredImageUrl;
    }

    public static final class Builder {
        private String id;
        private String title;
        private String slug;
        private String description;
        private String body;
        private Set<String> tags;
        private Date createdAt;
        private Date updatedAt;
        private boolean favourited;
        private long favouritesCount;
        private UserResponse author;
        private ArticleStatus status;
        private String featuredImageUrl;

        public Builder() {
        }

        public Builder withId(String val) {
            this.id = val;
            return this;
        }

        public Builder withTitle(String val) {
            title = val;
            return this;
        }

        public Builder withSlug(String val) {
            slug = val;
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

        public Builder withCreatedAt(Date val) {
            createdAt = val;
            return this;
        }

        public Builder withUpdatedAt(Date val) {
            updatedAt = val;
            return this;
        }

        public Builder withFavourited(boolean val) {
            favourited = val;
            return this;
        }

        public Builder withFavouritesCount(long val) {
            favouritesCount = val;
            return this;
        }

        public Builder withAuthor(UserResponse val) {
            author = val;
            return this;
        }

        public Builder withStatus(ArticleStatus val) {
            status = val;
            return this;
        }

        public Builder withFeaturedImageUrl(String val) {
            featuredImageUrl = val;
            return this;
        }


        public ArticleResponse build() {
            return new ArticleResponse(this);
        }
    }
}
