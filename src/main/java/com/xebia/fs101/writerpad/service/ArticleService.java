package com.xebia.fs101.writerpad.service;


import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.repositories.ArticleRepository;
import com.xebia.fs101.writerpad.requests.ArticleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;


    public Article save(Article article) {
        return this.articleRepository.save(article);
    }

    public Article findById(UUID uuid) {
        return this.articleRepository.findById(uuid)
                .orElseThrow(() -> new RuntimeException("No Article Found"));
    }

    public Article update(ArticleRequest articleRequest, String slugUuid) {
        Article article = this.findById(toUuid(slugUuid));
        Article articleToBeUpdated = article.update(articleRequest);
        return this.articleRepository.save(articleToBeUpdated);

    }

    public UUID toUuid(String input) {
        if (input.length() > 36) {
            return UUID.fromString(input.substring(input.length() - 36));
        } else {
            throw new IllegalArgumentException("slug uuid is invalid");
        }
    }

    public List<Article> findAll(Pageable pageable) {
        return this.articleRepository.findAll(pageable).getContent();
    }

    public void delete(String slugUuid) {
        UUID uuid = toUuid(slugUuid);
        if (Objects.nonNull(findById(uuid))) {
            this.articleRepository.deleteById(uuid);
        }
    }
}
