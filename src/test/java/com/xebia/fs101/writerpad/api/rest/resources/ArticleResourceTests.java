package com.xebia.fs101.writerpad.api.rest.resources;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.fs101.writerpad.api.rest.representations.ArticleRequest;
import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.repositories.ArticleRepository;
import com.xebia.fs101.writerpad.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class ArticleResourceTests {

    @SpyBean
    private ArticleService articleService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void mock_mvc_should_not_be_null() {
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void should_create_article_when_mandatory_request_data_is_provided() throws Exception {
/*
        String json = "{\n" +
                "  \"title\": \"How to learn Spring Boot\",\n" +
                "  \"description\": \"Ever wonder how?\",\n" +
                "  \"body\": \"You have to believe\",\n" +
                "  \"tags\": [\"java\", \"Spring Boot\", \"tutorial\"],\n" +
                "  \"featuredImageUrl\": \"url of the featured image\"\n" +
                "}";

   mockMvc.perform(post("/api/articles")
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("How to learn Spring Boot")));
*/


        ArticleRequest articleRequest = new ArticleRequest.Builder()
                .withTitle("How to learn Spring Boot")
                .withBody("You have to believe")
                .withDescription("Ever wonder how?")
                .build();
        String json = objectMapper.writeValueAsString(articleRequest);

        mockMvc.perform(
                post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value("How to learn Spring Boot"))
                .andExpect(jsonPath("$.slug").value("how-to-learn-spring-boot"))
                .andExpect(jsonPath("$.description").value("Ever wonder how?"))
                .andExpect(jsonPath("$.body").value("You have to believe"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.favorited").isBoolean())
                .andExpect(jsonPath("$.favorited").value(false))
                .andExpect(jsonPath("$.favoritesCount").value(0));
    }

   /* @Test
    void when_i_pass_article_request_title_blank_should_give_status_bad_request() throws Exception {
        String json = "{\n" +
                "  \"title\": ,\n" +
                "  \"description\": \"Ever wonder how?\",\n" +
                "  \"body\": \"You have to believe\",\n" +
                "  \"tags\": [\"java\", \"Spring Boot\", \"tutorial\"],\n" +
                "  \"featuredImageUrl\": \"url of the featured image\"\n" +
                "}";
        mockMvc.perform(post("/api/articles")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }*/

    @Test
    void should_create_article_when_mandatory_and_optional_request_data_is_provided() throws Exception {
        ArticleRequest articleRequest = new ArticleRequest.Builder()
                .withTitle("How to learn Spring Boot")
                .withBody("You have to believe")
                .withDescription("Ever wonder how?")
                .withTags(new HashSet<>(Arrays.asList("java", "Spring Boot", "tutorial")))
                .build();
        String json = objectMapper.writeValueAsString(articleRequest);
        mockMvc.perform(
                post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags").value(containsInAnyOrder("java", "spring-boot", "tutorial")));
    }

    @Test
    void should_give_400_bad_request_when_creating_article_without_all_required_fields() throws Exception {
        ArticleRequest articleRequest = new ArticleRequest.Builder()
                .build();
        String json = objectMapper.writeValueAsString(articleRequest);
        mockMvc.perform(
                post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_give_400_bad_request_when_creating_article_with_empty_fields() throws Exception {
        ArticleRequest articleRequest = new ArticleRequest.Builder()
                .withTitle("")
                .withDescription("")
                .withBody("")
                .build();
        String json = objectMapper.writeValueAsString(articleRequest);
        mockMvc.perform(
                post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    void when_i_pass_article_request_without_title_should_give_status_internal_server_error() throws Exception {

/*
        when(articleService.save(ArgumentMatchers.any())).thenThrow(new RuntimeException());
*/

        doThrow(new RuntimeException()).when(articleService).save(ArgumentMatchers.any());

        String json = "{\n" +
                "  \"title\": \"How to learn Spring Boot\",\n" +
                "  \"description\": \"Ever wonder how?\",\n" +
                "  \"body\": \"You have to believe\",\n" +
                "  \"should_get_500\": [\"java\", \"Spring Boot\", \"tutorial\"],\n" +
                "  \"featuredImageUrl\": \"url of the featured image\"\n" +
                "}";
        mockMvc.perform(post("/api/articles")
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }


    @Test
    public void should_update_article() throws Exception {

      /*  ArticleRequest articleRequest = new ArticleRequest.Builder()
                .withTitle("How to learn Spring Boot")
                .withBody("You have to believe")
                .withDescription("Ever wonder how?")
                .build();

        String json = objectMapper.writeValueAsString(articleRequest);
        *//*String json = "{\n" +
                "  \"title\": \"How to learn Spring Boot\",\n" +
                "  \"description\": \"Ever wonder how?\",\n" +
                "  \"body\": \"You have to believe\",\n" +
                "  \"tags\": [\"java\", \"Spring Boot\", \"tutorial\"],\n" +
                "  \"featuredImageUrl\": \"url of the featured image\"\n" +
                "}";*//*


        ResultActions resultActions = mockMvc.perform(post("/api/articles")
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("How to learn Spring Boot")));



        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        Article article = objectMapper.readValue(contentAsString,Article.class);
        String uuid= article.getSlug()+"-"+article.getId();
       // String uuid=contentAsString.substring(7,43);
        json = "{\n" +
                "    \"title\": \"How to learn Spring Boot by building an app\"\n" +
                "}";
        String uri="/api/articles/how-to-learn-spring-boot-"+uuid;
        mockMvc.perform( patch( uri )
                .contentType( MediaType.APPLICATION_JSON_VALUE )
                .content( json )
        ).andExpect( status().isOk() );*/

        Article article = new Article.Builder()
                .withBody("spring boot")
                .withDescription("application")
                .withTitle("spring boot application")
                .build();
        Article savedArticle = articleRepository.save(article);

        ArticleRequest articleRequest = new ArticleRequest.Builder()
                .withTitle("spring")
                .build();

        String id = String.format("%s-%s", savedArticle.getSlug(), savedArticle.getId());
        String json = objectMapper.writeValueAsString(articleRequest);
        this.mockMvc.perform(patch("/api/articles/{slug_id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").value(article.getId().toString()))
                .andExpect(jsonPath("$.title").value("spring"))
                .andExpect(jsonPath("$.slug").value("spring"))
                .andExpect(jsonPath("$.description").value("application"))
                .andExpect(jsonPath("$.body").value("spring boot"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").value(not(savedArticle.getUpdatedAt())))
                .andExpect(jsonPath("$.favorited").isBoolean())
                .andExpect(jsonPath("$.favorited").value(false))
                .andExpect(jsonPath("$.favoritesCount").value(0));
        ;


    }

    @Test
    public void should_get_article_when_i_provide_valid_id() throws Exception {

        Article article = new Article.Builder()
                .withBody("spring boot")
                .withDescription("application")
                .withTitle("spring boot application")
                .withUpdatedAt()
                .build();
        Article savedArticle = articleRepository.save(article);


        mockMvc.perform(get("/api/articles/{slug_id}", savedArticle.getSlug() + "-" + savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").value(savedArticle.getId().toString()))
                .andExpect(jsonPath("$.title").value("spring boot application"))
                .andExpect(jsonPath("$.slug").value("spring-boot-application"))
                .andExpect(jsonPath("$.description").value("application"))
                .andExpect(jsonPath("$.body").value("spring boot"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.favorited").isBoolean())
                .andExpect(jsonPath("$.favorited").value(false))
                .andExpect(jsonPath("$.favoritesCount").value(0));
    }


    @Test
    void should_list_all_articles() throws Exception {
        Article article1 = createArticle("Title1", "Description1", "body1");
        Article article2 = createArticle("Title2", "description2", "body2");
        Article article3 = createArticle("Title3", "description3", "body3");
        articleRepository.saveAll(Arrays.asList(article1, article2, article3));
        this.mockMvc.perform(get("/api/articles"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void should_list_all_articles_with_pagination() throws Exception {
        Article article1 = createArticle("Title1", "Description1", "body1");
        Article article2 = createArticle("Title2", "description2", "body2");
        Article article3 = createArticle("Title3", "description3", "body3");
        articleRepository.saveAll(Arrays.asList(article1, article2, article3));
        this.mockMvc.perform(get("/api/articles?page=0&size=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    private Article createArticle(String title, String description, String body) {
        return new Article.Builder()
                .withTitle(title)
                .withDescription(description)
                .withBody(body)
                .build();
    }


    @Test
    void should_delete_an_article() throws Exception {
        Article article = new Article.Builder()
                .withTitle("spring")
                .withBody("appl")
                .withDescription("boot")
                .build();
        Article savedArticle = articleRepository.save(article);
        String id = String.format("%s-%s", savedArticle.getSlug(), savedArticle.getId());
        this.mockMvc.perform(delete("/api/articles/{slug_id}", id)
        ).andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void should_not_delete_an_article() throws Exception {
        String id = "abc" + "-" + UUID.randomUUID().toString();
        this.mockMvc.perform(delete("/api/articles/{slug_id}", id)
        ).andDo(print())
                .andExpect(status().isNotFound());
    }

}