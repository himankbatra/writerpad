package com.xebia.fs101.writerpad.api.rest.resources;

import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.requests.ArticleRequest;
import com.xebia.fs101.writerpad.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.List;

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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PatchMapping(path = "/{slug_uuid}")
    public ResponseEntity<Article> update(@RequestBody ArticleRequest articleRequest,
                                          @PathVariable("slug_uuid") final String slugUuid) {
        Article updatedArticle = this.articleService.update(articleRequest, slugUuid);
        return new ResponseEntity<>(updatedArticle, HttpStatus.OK);
    }


    @GetMapping(path = "/{slug_uuid}")
    public ResponseEntity<Article> get(@PathVariable("slug_uuid") final String slugUuid) {
        Article foundArticle = this.articleService.findById(this.articleService.toUuid(slugUuid));
        return new ResponseEntity<>(foundArticle, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Article>> getAll(Pageable pageable) {
        List<Article> foundArticle = this.articleService.findAll(pageable);
        return new ResponseEntity<>(foundArticle, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{slug_uuid}")
    public ResponseEntity<Void> delete(@PathVariable("slug_uuid") final String slugUuid) {
        this.articleService.delete(slugUuid);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
