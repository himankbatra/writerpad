package com.xebia.fs101.writerpad.api.rest.resources;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.fs101.writerpad.api.rest.representations.ArticleRequest;
import com.xebia.fs101.writerpad.api.rest.representations.CommentRequest;
import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.domain.Comment;
import com.xebia.fs101.writerpad.repositories.ArticleRepository;
import com.xebia.fs101.writerpad.repositories.CommentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
 class CommentResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @AfterEach
    void tearDown() {
        this.commentRepository.deleteAll();
        this.articleRepository.deleteAll();
    }

    @Test
    void should_create_comment_for_provided_article_slug_id_when_mandatory_and_optional_request_data_is_provided() throws Exception {


        Article article = new Article.Builder()
                .withTitle("spring")
                .withBody("appl")
                .withDescription("boot")
                .build();
        Article savedArticle = articleRepository.save(article);
        String id = String.format("%s-%s", savedArticle.getSlug(), savedArticle.getId());

        CommentRequest commentRequest = new CommentRequest("Awesome Article");

        String json = objectMapper.writeValueAsString(commentRequest);
        mockMvc.perform(
                post("/api/articles/{slug_id}/comments", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.body").isNotEmpty())
                .andExpect(jsonPath("$.body").value("Awesome Article"))
                .andExpect(jsonPath("$.ipAddress").isNotEmpty());
    }

    @Test
    void should_give_400_bad_request_when_creating_comment_for_provided_article_slug_id_with_empty_fields() throws Exception {
        Article article = new Article.Builder()
                .withTitle("spring")
                .withBody("appl")
                .withDescription("boot")
                .build();
        Article savedArticle = articleRepository.save(article);
        String id = String.format("%s-%s", savedArticle.getSlug(), savedArticle.getId());

        CommentRequest commentRequest = new CommentRequest("");

        String json = objectMapper.writeValueAsString(commentRequest);
        mockMvc.perform(
                post("/api/articles/{slug_id}/comments", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    void should_get_400_and_not_post_a_comment_if_spam_word_exist_in_content() throws Exception {
        ArticleRequest articleRequest = new ArticleRequest.Builder()
                .withBody(" body")
                .withTitle("title")
                .withDescription("description")
                .build();
        System.out.println(articleRequest.toArticle());
        Article savedArticle = articleRepository.save(articleRequest.toArticle());
        String slugId = String.format("%s_%s", savedArticle.getSlug(), savedArticle.getId());
        CommentRequest commentRequest=new CommentRequest("semen");
        String json=objectMapper.writeValueAsString(commentRequest);
        mockMvc.perform(post("/api/articles/{slug_id}/comments",slugId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_list_all_comments_when_valid_article_id_given() throws Exception {
        Article article = new Article.Builder()
                .withBody("Body")
                .withTitle("Title")
                .withDescription("Desc")
                .build();
        Article saved = articleRepository.save(article);
        String id = String.format("%s-%s", saved.getSlug(), saved.getId());
        Comment comment1 = new Comment("Comment1", "10.1.1.1", article);
        Comment comment2 = new Comment("Comment2", "10.1.1.1", article);
        Comment comment3 = new Comment("Comment3", "10.1.1.1", article);
        commentRepository.saveAll(Arrays.asList(comment1, comment2, comment3));
        this.mockMvc.perform(get("/api/articles/{slug_id}/comments/", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void should_delete_comment_when_valid_id_passed() throws Exception {
        Article article = new Article.Builder()
                .withBody("Body")
                .withTitle("Title")
                .withDescription("Desc")
                .build();
        Article saved = articleRepository.save(article);
        String id = saved.getSlug() + "-" + saved.getId();
        Comment comment = new Comment("Comment1", "10.1.1.1", saved);
        Comment savedComment = commentRepository.save(comment);
        Long commentId = savedComment.getId();
        mockMvc.perform(delete("/api/articles/{slug_id}/comments/{comment_id}", id, commentId))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void should_not_delete_comment_when_invalid_id_passed() throws Exception {
        Article article = new Article.Builder()
                .withBody("Body")
                .withTitle("Title")
                .withDescription("Desc")
                .build();
        Article saved = articleRepository.save(article);
        String id = "abc" + "-" + UUID.randomUUID().toString();
        Comment comment = new Comment("Comment1", "10.1.1.1", saved);
        Comment savedComment = commentRepository.save(comment);
        Long commentId = savedComment.getId();
        mockMvc.perform(delete("/api/articles/{slug_id}/comments/{comment_id}", id, commentId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}