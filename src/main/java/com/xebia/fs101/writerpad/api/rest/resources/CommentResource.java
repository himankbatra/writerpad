package com.xebia.fs101.writerpad.api.rest.resources;

import com.xebia.fs101.writerpad.api.rest.representations.CommentRequest;
import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.domain.Comment;
import com.xebia.fs101.writerpad.services.ArticleService;
import com.xebia.fs101.writerpad.services.CommentService;
import com.xebia.fs101.writerpad.services.SpamChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static com.xebia.fs101.writerpad.utils.StringUtils.toUuid;

@RestController
@RequestMapping(path = "/api/articles/{slug_id}/comments")
public class CommentResource {

    @Autowired
    private CommentService commentService;
    @Autowired
    private SpamChecker spamChecker;

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseEntity<Comment> create(@Valid @RequestBody CommentRequest commentRequest,
                                          @PathVariable("slug_id") final String slugId,
                                          HttpServletRequest request) {

        Optional<Article> optionalArticle = this.articleService.findById(toUuid(slugId));
        if (optionalArticle.isPresent()) {
            if (!this.spamChecker.isSpam(commentRequest.getBody())) {
                Comment savedComment = this.commentService
                        .save(commentRequest.toComment(request.getRemoteAddr(),
                                optionalArticle.get()));
                return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
            }
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.notFound().build();

    }

    @GetMapping
    public ResponseEntity<List<Comment>> getAll(@PathVariable("slug_id") final String slugId) {
        Optional<Article> optionalArticle = this.articleService.findById(toUuid(slugId));
        if (optionalArticle.isPresent()) {
            List<Comment> foundComment =
                    this.commentService.findAllByArticleId(optionalArticle.get().getId());
            return new ResponseEntity<>(foundComment, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final Long id,
                                       @PathVariable("slug_id") final String slugId) {
        Optional<Article> optionalArticle = this.articleService.findById(toUuid(slugId));
        if (optionalArticle.isPresent()) {
        return this.commentService.delete(id)
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.notFound().build();
    }

}
