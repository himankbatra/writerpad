package com.xebia.fs101.writerpad.domain;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue
    private UUID id;

    private String title;

    private String slug;

    @Column(length = 4000)
    private String body;


    private String description;

    @ElementCollection
    @CollectionTable(name = "tags", joinColumns = @JoinColumn(name = "id"))
    private Set<String> tags;

    @Override
    public String toString() {
        return "Article{"
                + "id=" + id
                + ", title='" + title + '\''
                + ", slug='" + slug + '\''
                + ", body='" + body + '\''
                + ", description='" + description + '\''
                + ", tags=" + tags
                + ", featuredImageUrl='" + featuredImageUrl + '\''
                + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt
                + ", isFavorite=" + isFavorite
                + ", favoriteCount=" + favoriteCount
                + '}';
    }

    private String featuredImageUrl;

    private Date createdAt;

    private Date updatedAt;

    private boolean isFavorite = false;

    private int favoriteCount = 0;

    private Article(Builder builder) {
        id = builder.id;
        title = builder.title;
        slug = builder.slug;
        body = builder.body;
        description = builder.description;
        tags = builder.tags;
        featuredImageUrl = builder.featuredImage;
        createdAt = builder.createdAt;
        updatedAt = builder.updatedAt;
        isFavorite = builder.isFavorite;
        favoriteCount = builder.favoriteCount;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSlug() {
        return slug;
    }

    public String getBody() {
        return body;
    }

    public String getDescription() {
        return description;
    }

    public Set<String> getTags() {
        return tags;
    }

    public String getFeaturedImageUrl() {
        return featuredImageUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public static final class Builder {
        private UUID id;
        private String title;
        private String slug;
        private String body;
        private String description;
        private Set<String> tags;
        private String featuredImage;
        private String imageUrl;
        private Date createdAt;
        private Date updatedAt;
        private boolean isFavorite;
        private int favoriteCount;

        public Builder() {
        }

        public Builder withId(UUID val) {
            id = val;
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

        public Builder withBody(String val) {
            body = val;
            return this;
        }

        public Builder withDescription(String val) {
            description = val;
            return this;
        }

        public Builder withTags(Set<String> val) {
            tags = val;
            return this;
        }

        public Builder withFeaturedImage(String val) {
            featuredImage = val;
            return this;
        }

        public Builder withImageUrl(String val) {
            imageUrl = val;
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

        public Builder withIsFavorite(boolean val) {
            isFavorite = val;
            return this;
        }

        public Builder withFavoriteCount(int val) {
            favoriteCount = val;
            return this;
        }

        public Article build() {
            return new Article(this);
        }
    }
}
