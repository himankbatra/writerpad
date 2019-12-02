package com.xebia.fs101.writerpad.services;


import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.domain.ArticleStatus;
import com.xebia.fs101.writerpad.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.xebia.fs101.writerpad.utils.StringUtils.toUuid;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;


    public Article save(Article article) {
        return this.articleRepository.save(article);
    }

    public Optional<Article> findById(UUID uuid) {
        return this.articleRepository.findById(uuid);
    }

    public Optional<Article> update(String slugId, Article copyFrom) {
        Optional<Article> optionalArticle = this.findById(toUuid(slugId));
        return optionalArticle
                .map(article -> this.articleRepository.save(article.update(copyFrom)));
    }


    public List<Article> findAll(String status, Pageable pageable) {
        if (Objects.nonNull(status)) {
            return this.articleRepository.findAllByStatus(ArticleStatus.valueOf(status), pageable);
        }
        return this.articleRepository.findAll(pageable).getContent();
    }

    public boolean delete(String slugId) {
        UUID id = toUuid(slugId);
        Optional<Article> optionalArticle = this.findById(id);
        if (optionalArticle.isPresent()) {
            this.articleRepository.deleteById(id);
            return true;
        }
        return false;

    }


    public Optional<Article> publish(String slugId) {
        Optional<Article> optionalArticle = this.findById(toUuid(slugId));
        return optionalArticle
                .filter(article -> article.getStatus() != ArticleStatus.PUBLISH)
                .map(article -> this.articleRepository.save(article.publish()));
    }
}
