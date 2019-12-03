package com.xebia.fs101.writerpad.api.rest.resources;

import com.xebia.fs101.writerpad.api.rest.representations.ArticleRequest;
import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.services.ArticleService;
import com.xebia.fs101.writerpad.services.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static com.xebia.fs101.writerpad.utils.StringUtils.toUuid;

@RestController
@RequestMapping(path = "/api/articles")
public class ArticleResource {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private MailService mailService;

    @PostMapping
    public ResponseEntity<Article> create(@Valid @RequestBody ArticleRequest articleRequest) {

        try {
            Article savedArticle = this.articleService.save(articleRequest.toArticle());
            return new ResponseEntity<>(savedArticle, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PatchMapping(path = "/{slug_id}")
    public ResponseEntity<Article> update(@RequestBody ArticleRequest articleRequest,
                                          @PathVariable("slug_id") final String slugId) {
        Optional<Article> optionalUpdatedArticle =
                this.articleService.update(slugId, articleRequest.toArticle());
        return optionalUpdatedArticle.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/{slug_id}/publish")
    public ResponseEntity<Article> publish(@PathVariable("slug_id") final String slugId) {
        boolean publish = this.articleService.publish(slugId);
            this.mailService.sendEmail("publish article"
                    , "Publishing an article with Article Id " + toUuid(slugId));
        return publish
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.badRequest().build();
    }



    @GetMapping(path = "/{slug_id}")
    public ResponseEntity<Article> get(@PathVariable("slug_id") final String slugId) {
        Optional<Article> optionalFoundArticle = this.articleService.findById(toUuid(slugId));
        return optionalFoundArticle.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping
    public ResponseEntity<List<Article>> getAll(@RequestParam(name = "status", required = false)
                                                        String status, Pageable pageable) {

        List<Article> foundArticle = this.articleService.findAll(status, pageable);
        return new ResponseEntity<>(foundArticle, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{slug_id}")
    public ResponseEntity<Void> delete(@PathVariable("slug_id") final String slugId) {
        return this.articleService.delete(slugId)
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(MailException.class)
    void mailException(Exception ex)
    {
        // log error
    }

}
