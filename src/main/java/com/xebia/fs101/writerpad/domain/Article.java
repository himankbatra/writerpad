package com.xebia.fs101.writerpad.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xebia.fs101.writerpad.utils.StringUtils;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String title;

    @Transient
    private String slug;

    @Column(length = 4000)
    private String body;


    private String description;

    @ElementCollection
    @CollectionTable(name = "tags", joinColumns = @JoinColumn(name = "id"))
    private Set<String> tags;


    private String featuredImageUrl;

    @Column(updatable = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    private boolean favorited = false;

    private long favoritesCount = 0;


    @JsonIgnore
    @OneToMany(mappedBy = "article")
    private List<Comment> comments;

    @Enumerated(EnumType.STRING)
    private ArticleStatus status;


    public Article update(Article copyFrom) {

        if (Objects.nonNull(copyFrom.getTitle())) {
            this.title = copyFrom.getTitle();
        }
        if (Objects.nonNull(copyFrom.getBody())) {
            this.body = copyFrom.getBody();
        }
        if (Objects.nonNull(copyFrom.getDescription())) {
            this.description = copyFrom.getDescription();
        }
        if (copyFrom.getTags().size() > 0) {
            this.tags = copyFrom.getTags();
        }
        if (Objects.nonNull(copyFrom.getStatus())) {
            this.status = copyFrom.getStatus();
        }

        this.updatedAt = new Date();
        return this;
    }

    public Article() {
    }

    private Article(Builder builder) {
        title = builder.title;
        body = builder.body;
        description = builder.description;
        tags = builder.tags;
        featuredImageUrl = builder.featuredImageUrl;
        updatedAt = builder.updatedAt;
        favorited = builder.favorited;
        favoritesCount = builder.favoritesCount;
        status = builder.status;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSlug() {
        return Objects.nonNull(this.title) ? StringUtils.slugify(this.title)
                : this.slug;

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

    public boolean isFavorited() {
        return favorited;
    }

    public long getFavoritesCount() {
        return favoritesCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public ArticleStatus getStatus() {
        return status;
    }

    public Article publish() {
        this.status = ArticleStatus.PUBLISH;
        return this;
    }

    public static final class Builder {
        private String title;
        private String body;
        private String description;
        private Set<String> tags;
        private String featuredImageUrl;
        private Date updatedAt;
        private boolean favorited;
        private long favoritesCount;
        public ArticleStatus status = ArticleStatus.DRAFT;

        public Builder() {
        }


        public Builder withTitle(String val) {
            title = val;
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

        public Builder withFeaturedImageUrl(String val) {
            featuredImageUrl = val;
            return this;
        }


        public Builder withUpdatedAt() {
            updatedAt = new Date();
            return this;
        }

        public Builder withFavorited(boolean val) {
            favorited = val;
            return this;
        }

        public Builder withFavoritesCount(long val) {
            favoritesCount = val;
            return this;
        }

        public Builder withStatus(ArticleStatus val) {
            status = val;
            return this;
        }

        public Article build() {
            return new Article(this);
        }
    }


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
                + ", isFavorite=" + favorited
                + ", favoriteCount=" + favoritesCount
                + ", status=" + status
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;

        if (favorited != article.favorited) return false;
        if (favoritesCount != article.favoritesCount) return false;
        if (id != null ? !id.equals(article.id) : article.id != null) return false;
        if (title != null ? !title.equals(article.title) : article.title != null) return false;
        if (slug != null ? !slug.equals(article.slug) : article.slug != null) return false;
        if (body != null ? !body.equals(article.body) : article.body != null) return false;
        if (description != null
                ? !description.equals(article.description) : article.description != null)
            return false;
        if (tags != null ? !tags.equals(article.tags) : article.tags != null) return false;
        if (featuredImageUrl != null
                ? !featuredImageUrl.equals(article.featuredImageUrl)
                : article.featuredImageUrl != null) return false;
        if (createdAt != null
                ? !createdAt.equals(article.createdAt) : article.createdAt != null) return false;
        if (updatedAt != null
                ? !updatedAt.equals(article.updatedAt) : article.updatedAt != null) return false;
        if (comments != null
                ? !comments.equals(article.comments) : article.comments != null) return false;
        return status == article.status;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (slug != null ? slug.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result
                + (featuredImageUrl != null ? featuredImageUrl.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        result = 31 * result + (favorited ? 1 : 0);
        result = 31 * result + (int) (favoritesCount ^ (favoritesCount >>> 32));
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
