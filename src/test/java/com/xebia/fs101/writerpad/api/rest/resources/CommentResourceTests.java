package com.xebia.fs101.writerpad.api.rest.resources;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.fs101.writerpad.api.rest.representations.ArticleRequest;
import com.xebia.fs101.writerpad.api.rest.representations.CommentRequest;
import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.repositories.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class CommentResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArticleRepository articleRepository;

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
                post("/api/articles/{slug_id}/comments",id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.body").isNotEmpty())
                .andExpect(jsonPath("$.body").value("Awesome Article"))
                .andExpect(jsonPath("$.ipAddress").isNotEmpty()) ;
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
                post("/api/articles/{slug_id}/comments",id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}