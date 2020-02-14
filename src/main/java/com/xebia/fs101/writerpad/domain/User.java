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
import javax.persistence.Transient;
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

    @Transient
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
        this.followingCount++;
    }


    public void addFollowers(String username) {
        this.followers.add(username);
        this.followerCount++;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public void unFollow() {
        this.followingCount--;
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
                + ", password='" + password + '\''
                + ", articles=" + articles
                + ", userRole=" + userRole
                + ", following=" + following
                + ", followerCount=" + followerCount
                + ", followingCount=" + followingCount
                + ", followers=" + followers
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userid != user.userid) return false;
        if (following != user.following) return false;
        if (followerCount != user.followerCount) return false;
        if (followingCount != user.followingCount) return false;
        if (username != null ? !username.equals(user.username) : user.username != null)
            return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null)
            return false;
        if (articles != null ? !articles.equals(user.articles) : user.articles != null)
            return false;
        if (userRole != user.userRole) return false;
        return followers != null ? followers.equals(user.followers)
                : user.followers == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (userid ^ (userid >>> 32));
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (articles != null ? articles.hashCode() : 0);
        result = 31 * result + (userRole != null ? userRole.hashCode() : 0);
        result = 31 * result + (following ? 1 : 0);
        result = 31 * result + (int) (followerCount ^ (followerCount >>> 32));
        result = 31 * result + (int) (followingCount ^ (followingCount >>> 32));
        result = 31 * result + (followers != null ? followers.hashCode() : 0);
        return result;
    }
}
