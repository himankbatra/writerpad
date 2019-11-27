package com.xebia.fs101.writerpad.repositories;


import com.xebia.fs101.writerpad.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface ArticleRepository extends JpaRepository<Article, UUID> {

}
