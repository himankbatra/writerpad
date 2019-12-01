package com.xebia.fs101.writerpad.repositories;


import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.domain.ArticleStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface ArticleRepository extends JpaRepository<Article, UUID> {

    List<Article> findAllByStatus(ArticleStatus status, Pageable pageable);
}

