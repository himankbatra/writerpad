package com.xebia.fs101.writerpad.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

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

    private User(Builder builder) {
        username = builder.username;
        email = builder.email;
        password = builder.password;
    }

    public static final class Builder {
        private String username;
        private String email;
        private String password;

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

    public User() {
    }


    public User(User other) {
        this.userid = other.userid;
        this.username = other.username;
        this.email = other.email;
        this.password = other.password;
    }

    @Override
    public String toString() {
        return "User{"
                + "userid=" + userid
                + ", username='" + username + '\''
                + ", email='" + email + '\''
                + '}';
    }
}