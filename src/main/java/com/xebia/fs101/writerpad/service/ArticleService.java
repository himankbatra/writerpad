package com.xebia.fs101.writerpad.service;


import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public Optional<Article> update(Article copyFrom, String slugId) {
        Optional<Article> optionalArticle = this.findById(toUuid(slugId));
        return optionalArticle
                .map(article -> this.articleRepository.save(article.update(copyFrom)));
    }


    public List<Article> findAll(Pageable pageable) {
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
/*        return optionalArticle.map(article -> {
            this.articleRepository.deleteById(article.getId());
            return true;
        }).orElse(false);*/

    }
}
