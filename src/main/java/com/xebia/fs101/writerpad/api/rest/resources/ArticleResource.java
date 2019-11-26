package com.xebia.fs101.writerpad.api.rest.resources;

import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.requests.ArticleRequest;
import com.xebia.fs101.writerpad.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/articles")
public class ArticleResource {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseEntity<Article> create(@Valid @RequestBody ArticleRequest articleRequest) {

        try {
            Article savedArticle = this.articleService.save(articleRequest.toArticle());
            return new ResponseEntity<>(savedArticle, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
