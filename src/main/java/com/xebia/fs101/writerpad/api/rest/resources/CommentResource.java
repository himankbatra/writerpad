package com.xebia.fs101.writerpad.api.rest.resources;

import com.xebia.fs101.writerpad.api.rest.representations.CommentRequest;
import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.domain.Comment;
import com.xebia.fs101.writerpad.services.ArticleService;
import com.xebia.fs101.writerpad.services.CommentService;
import com.xebia.fs101.writerpad.services.helpers.SpamChecker;
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

        Article article = this.articleService.findById(slugId);
        if (!this.spamChecker.isSpam(commentRequest.getBody())) {
            Comment savedComment = this.commentService
                    .save(commentRequest.toComment(request.getRemoteAddr(),
                            article));
            return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
        }
        return ResponseEntity.badRequest().build();

    }

    @GetMapping
    public ResponseEntity<List<Comment>> getAll(@PathVariable("slug_id") final String slugId) {
        Article article = this.articleService.findById(slugId);

        List<Comment> foundComment =
                this.commentService.findAllByArticleId(article.getId());
        return new ResponseEntity<>(foundComment, HttpStatus.OK);

    }


    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final Long id,
                                       @PathVariable("slug_id") final String slugId) {
        Article article = this.articleService.findById(slugId);
        return this.commentService.delete(id)
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
