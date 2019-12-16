package com.xebia.fs101.writerpad.api.rest.representations;

import com.xebia.fs101.writerpad.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserResponse {
    private String username;
    private boolean following;
    private long followerCount;
    private long followingCount;
    private List<ArticleResponse> articles;

    private static class ArticleResponse {
        private UUID id;
        private String title;

        public ArticleResponse(UUID id, String title) {
            this.id = id;
            this.title = title;
        }

        public UUID getId() {

            return id;
        }

        public String getTitle() {

            return title;
        }
    }


    public String getUsername() {

        return username;
    }

    public boolean isFollowing() {

        return following;
    }

    public long getFollowerCount() {

        return followerCount;
    }

    public long getFollowingCount() {

        return followingCount;
    }

    public List<ArticleResponse> getArticles() {

        return articles;
    }

    private UserResponse(Builder builder) {
        this.username = builder.username;
        this.following = builder.following;
        this.followerCount = builder.followerCount;
        this.followingCount = builder.followingCount;
        this.articles = builder.articles;
    }

    private static final class Builder {
        private String username;
        private boolean following;
        private long followerCount;
        private long followingCount;
        private List<ArticleResponse> articles;

        public Builder() {

        }

        public Builder withUsername(String username) {

            this.username = username;
            return this;
        }

        public Builder withFollowing(boolean following) {

            this.following = following;
            return this;
        }

        public Builder withFollowerCount(long followerCount) {

            this.followerCount = followerCount;
            return this;
        }

        public Builder withFollowingCount(long followingCount) {

            this.followingCount = followingCount;
            return this;
        }

        public Builder withArticles(List<ArticleResponse> articles) {

            this.articles = articles == null ? new ArrayList<>() : articles;
            return this;
        }

        public UserResponse build() {

            return new UserResponse(this);
        }
    }

    public static UserResponse from(User user) {

        return new UserResponse.Builder()
                .withUsername(user.getUsername())
                .withArticles(user.getArticles().stream()
                        .map(e -> new ArticleResponse(e.getId(), e.getTitle()))
                        .collect(Collectors.toList()))
                .withFollowerCount(user.getFollowerCount())
                .withFollowingCount(user.getFollowingCount())
                .withFollowing(user.isFollowing())
                .build();
    }


}
