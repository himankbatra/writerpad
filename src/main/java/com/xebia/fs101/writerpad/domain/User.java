package com.xebia.fs101.writerpad.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userid;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    @JsonBackReference
    @OneToMany(mappedBy = "user")
    private List<Article> articles;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private boolean following = false;

    private long followerCount = 0;

    private long followingCount = 0;

    @ElementCollection
    @CollectionTable(name = "followers", joinColumns = @JoinColumn(name = "userid"))
    private Set<String> followers;


    private User(Builder builder) {
        username = builder.username;
        email = builder.email;
        password = builder.password;
        userRole = builder.userRole;
        following = builder.following;
        followerCount = builder.followerCount;
        followingCount = builder.followingCount;
        followers = new HashSet<>();
    }

    public void follow() {
        this.following = true;
        this.followingCount++;
    }

    public void addFollowers(String username) {
        this.followers.add(username);
        this.followerCount++;
    }

    public void unFollow() {
        this.followingCount--;
        this.following = this.followingCount != 0;
    }

    public void removeFollowers(String username) {
        this.followers.remove(username);
        this.followerCount--;
    }


    public static final class Builder {

        private String username;
        private String email;
        private String password;
        private UserRole userRole = UserRole.WRITER;
        private boolean following = false;
        private long followerCount = 0;
        private long followingCount = 0;


        public Builder() {
        }

        public Builder withUsername(String val) {
            username = val;
            return this;
        }

        public Builder withEmail(String val) {
            email = val;
            return this;
        }

        public Builder withPassword(String val) {
            password = val;
            return this;
        }

        public Builder withUserRole(UserRole val) {
            userRole = val;
            return this;
        }

        public Builder withFollowing(boolean val) {
            following = val;
            return this;
        }

        public Builder withFollowerCount(long val) {
            followerCount = val;
            return this;
        }

        public Builder withFollowingCount(long val) {
            followingCount = val;
            return this;
        }

        public User build() {
            return new User(this);
        }

    }


    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }

    public long getUserid() {
        return userid;
    }

    public String getEmail() {
        return email;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public UserRole getUserRole() {
        return userRole;
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

    public Set<String> getFollowers() {
        return followers;
    }

    public User() {
    }


    public User(User other) {
        this.userid = other.userid;
        this.username = other.username;
        this.email = other.email;
        this.password = other.password;
        this.userRole = other.userRole;
    }

    @Override
    public String toString() {
        return "User{"
                + "userid=" + userid
                + ", username='" + username + '\''
                + ", email='" + email + '\''
                + ", userRole='" + userRole + '\''
                + '}';
    }


}
