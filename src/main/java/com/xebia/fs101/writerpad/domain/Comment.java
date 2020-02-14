package com.xebia.fs101.writerpad.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(updatable = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    private String body;

    private String ipAddress;

    @JsonManagedReference
    @ManyToOne
    private Article article;

    public Comment(String body, String ipAddress, Article article) {
        this.body = body;
        this.ipAddress = ipAddress;
        this.updatedAt = new Date();
        this.article = article;
    }

    public Comment() {
    }

    public long getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getBody() {
        return body;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Article getArticle() {
        return article;
    }
}
