package com.xebia.fs101.writerpad.api.rest.resources;

import com.xebia.fs101.writerpad.api.rest.representations.ArticleRequest;
import com.xebia.fs101.writerpad.api.rest.representations.ArticleResponse;
import com.xebia.fs101.writerpad.api.rest.representations.ReadingTimeResponse;
import com.xebia.fs101.writerpad.api.rest.representations.TagResponse;
import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.services.helpers.ReadingTime;
import com.xebia.fs101.writerpad.domain.User;
import com.xebia.fs101.writerpad.services.ArticleService;
import com.xebia.fs101.writerpad.services.helpers.ReadingTimeService;
import com.xebia.fs101.writerpad.services.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.xebia.fs101.writerpad.utils.StringUtils.toUuid;

@RestController
@RequestMapping(path = "/api/articles")
public class ArticleResource {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private MailService mailService;

    @Autowired
    private ReadingTimeService readingTimeService;

    @PostMapping
    public ResponseEntity<ArticleResponse> create(@AuthenticationPrincipal User user,
                                                  @Valid @RequestBody
                                                          ArticleRequest articleRequest) {
        try {
            Article savedArticle = this.articleService.save(articleRequest.toArticle(),
                    user);
            return new ResponseEntity<>(ArticleResponse.from(savedArticle),
                    HttpStatus.CREATED);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


    @PatchMapping(path = "/{slug_id}")
    public ResponseEntity<Article> update(@RequestBody ArticleRequest articleRequest,
                                          @PathVariable("slug_id") final String slugId) {
        Article updatedArticle =
                this.articleService.update(slugId, articleRequest.toArticle());
        return ResponseEntity.ok(updatedArticle);

    }

    @PostMapping(path = "/{slug_id}/publish")
    public ResponseEntity<Article> publish(@PathVariable("slug_id") final String slugId) {
        boolean publish = this.articleService.publish(slugId);
        if (!publish) {
            return ResponseEntity.badRequest().build();
        }
        this.mailService.sendEmail("publish article"
                , "Publishing an article with Article Id " + toUuid(slugId));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping(path = "/{slug_id}")
    public ResponseEntity<Article> get(@PathVariable("slug_id") final String slugId) {
        Article foundArticle =
                this.articleService.findById(slugId);
        return ResponseEntity.ok(foundArticle);
    }


    @GetMapping
    public ResponseEntity<List<Article>> getAll(@RequestParam(name = "status",
            required = false)
                                                        String status,
                                                Pageable pageable) {

        List<Article> foundArticle = this.articleService.findAll(status, pageable);
        return new ResponseEntity<>(foundArticle, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{slug_id}")
    public ResponseEntity<Void> delete(@PathVariable("slug_id") final String slugId) {
        this.articleService.delete(slugId);
        return ResponseEntity.noContent().build();

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(MailException.class)
    void mailException(Exception ex) {
        // log error
    }


    @GetMapping(path = "/{slug_id}/timetoread")
    public ResponseEntity<ReadingTimeResponse> timeToRead(@PathVariable("slug_id") String slugId) {
        Article article = this.articleService.findById(slugId);
        ReadingTime readingTime =
                this.readingTimeService.calculateReadingTime(article.getBody());
        return ResponseEntity.ok(new ReadingTimeResponse(slugId
                , readingTime));
    }


    @GetMapping(path = "/tags")
    public ResponseEntity<List<TagResponse>> getTags() {
        List<TagResponse> collect =
                this.articleService.getTagsWithCount().entrySet().stream()
                        .map(e -> new TagResponse(e.getKey(), e.getValue()))
                        .collect(Collectors.toList());
        return new ResponseEntity<>(collect, HttpStatus.OK);

    }

    @PutMapping(path = "/{slug_id}/favourite")
    public ResponseEntity<Void> favourite(@PathVariable(name = "slug_id") String slugId) {
        this.articleService.favourite(slugId);
        return ResponseEntity.noContent().build();

    }


    @DeleteMapping(path = "/{slug_id}/favourite")
    public ResponseEntity<Void> unFavourite(@PathVariable(name = "slug_id") String slugId) {
        this.articleService.unFavourite(slugId);
        return ResponseEntity.noContent().build();

    }


}
