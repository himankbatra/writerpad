package com.xebia.fs101.writerpad.api.rest.resources;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.fs101.writerpad.api.rest.representations.ArticleRequest;
import com.xebia.fs101.writerpad.api.rest.representations.UserRequest;
import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.domain.ArticleStatus;
import com.xebia.fs101.writerpad.domain.User;
import com.xebia.fs101.writerpad.domain.UserRole;
import com.xebia.fs101.writerpad.repositories.ArticleRepository;
import com.xebia.fs101.writerpad.repositories.CommentRepository;
import com.xebia.fs101.writerpad.repositories.UserRepository;
import com.xebia.fs101.writerpad.services.ArticleService;
import org.hibernate.exception.JDBCConnectionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.isA;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ArticleResourceTests {


    @SpyBean
    private ArticleService articleService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    private User user;

    @BeforeEach
    void setUp() {
        UserRequest userRequest = new UserRequest.Builder()
                .withUsername("abc")
                .withPassword("abc@123")
                .withEmail("abc@123.com")
                .build();
        user = userRequest.toUser(passwordEncoder);
        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        this.commentRepository.deleteAll();
        this.articleRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    void mock_mvc_should_not_be_null() {
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void should_create_article_when_mandatory_request_data_is_provided() throws Exception {

        ArticleRequest articleRequest = new ArticleRequest.Builder()
                .withTitle("How to learn Spring Boot")
                .withBody("You have to believe")
                .withDescription("Ever wonder how?")
                .build();
        String json = objectMapper.writeValueAsString(articleRequest);

        mockMvc.perform(
                post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json).with(httpBasic("abc", "abc@123")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value("How to learn Spring Boot"))
                .andExpect(jsonPath("$.slug").value("how-to-learn-spring-boot"))
                .andExpect(jsonPath("$.description").value("Ever wonder how?"))
                .andExpect(jsonPath("$.body").value("You have to believe"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.favourited").isBoolean())
                .andExpect(jsonPath("$.favourited").value(false))
                .andExpect(jsonPath("$.favouritesCount").value(0))
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andExpect(jsonPath("$.featuredImageUrl").isNotEmpty())
                .andExpect(jsonPath("$.featuredImageUrl").isString())
                .andExpect(jsonPath("$.featuredImageUrl").value("test image"))
                .andExpect(jsonPath("$.author").hasJsonPath())
                .andExpect(jsonPath("$..username").value("abc"));
    }


    @Test
    void should_create_article_when_mandatory_and_optional_request_data_is_provided() throws Exception {
        ArticleRequest articleRequest = new ArticleRequest.Builder()
                .withTitle("How to learn Spring Boot")
                .withBody("You have to believe")
                .withDescription("Ever wonder how?")
                .withTags(new HashSet<>(Arrays.asList("java", "Spring Boot", "tutorial")))
                .withFeaturedImageUrl("\"https://images.unsplash" +
                        ".com/photo-1575483893529-3a9a8f8f2da5?ixlib=rb-1.2" +
                        ".1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid" +
                        "=eyJhcHBfaWQiOjEwNTkwM30\"")
                .build();
        String json = objectMapper.writeValueAsString(articleRequest);
        mockMvc.perform(
                post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json).with(httpBasic("abc", "abc@123")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags")
                        .value(containsInAnyOrder("java", "spring-boot", "tutorial")))
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andExpect(jsonPath("$.featuredImageUrl").isNotEmpty())
                .andExpect(jsonPath("$.featuredImageUrl").isString())
                .andExpect(jsonPath("$.featuredImageUrl").value(
                        "\"https://images.unsplash" +
                                ".com/photo-1575483893529-3a9a8f8f2da5?ixlib=rb-1.2" +
                                ".1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max" +
                                "&ixid" +
                                "=eyJhcHBfaWQiOjEwNTkwM30\""))
                .andExpect(jsonPath("$.author").hasJsonPath())
                .andExpect(jsonPath("$..username").value("abc"));
        ;
    }

    @Test
    void should_give_400_bad_request_when_creating_article_without_all_required_fields() throws Exception {
        ArticleRequest articleRequest = new ArticleRequest.Builder()
                .build();
        String json = objectMapper.writeValueAsString(articleRequest);
        mockMvc.perform(
                post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json).with(httpBasic("abc", "abc@123")))
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
                        .content(json).with(httpBasic("abc", "abc@123")))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    void should_give_500_bad_request_when_creating_article_with_invalid_data() throws Exception {

        doThrow(new JDBCConnectionException(" exception", null)).when(articleService).save(any(), any());

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
                .contentType(MediaType.APPLICATION_JSON).with(httpBasic("abc", "abc@123"
                )))
                .andExpect(status().isInternalServerError());
    }


    @Test
    void should_update_article() throws Exception {

        Article article = new Article.Builder()
                .withBody("spring boot")
                .withDescription("application")
                .withTitle("spring boot application")
                .build();
        article.setUser(user);
        Article savedArticle = articleRepository.save(article);

        ArticleRequest articleRequest = new ArticleRequest.Builder()
                .withTitle("spring")
                .build();

        String id = String.format("%s-%s", savedArticle.getSlug(), savedArticle.getId());
        String json = objectMapper.writeValueAsString(articleRequest);
        this.mockMvc.perform(patch("/api/articles/{slug_id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json).with(httpBasic("abc", "abc@123")))
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
                .andExpect(jsonPath("$.favourited").isBoolean())
                .andExpect(jsonPath("$.favourited").value(false))
                .andExpect(jsonPath("$.favouritesCount").value(0))
                .andExpect(jsonPath("$.featuredImageUrl").isNotEmpty())
                .andExpect(jsonPath("$.featuredImageUrl").isString())
                .andExpect(jsonPath("$.status").value("DRAFT"));


    }

    @Test
    void should_get_article_when_i_provide_valid_id() throws Exception {

        Article article = new Article.Builder()
                .withBody("spring boot")
                .withDescription("application")
                .withTitle("spring boot application")
                .withUpdatedAt()
                .build();
        article.setUser(user);
        Article savedArticle = articleRepository.save(article);

        mockMvc.perform(get("/api/articles/{slug_id}",
                savedArticle.getSlug() + "-" + savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON).with(httpBasic("abc", "abc@123"
                )))
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
                .andExpect(jsonPath("$.favourited").isBoolean())
                .andExpect(jsonPath("$.favourited").value(false))
                .andExpect(jsonPath("$.favouritesCount").value(0))
                .andExpect(jsonPath("$.status").value("DRAFT"));
    }


    @Test
    void should_list_all_articles() throws Exception {
        Article article1 = createArticle("Title1", "Description1", "body1");
        Article article2 = createArticle("Title2", "description2", "body2");
        Article article3 = createArticle("Title3", "description3", "body3");
        articleRepository.saveAll(Arrays.asList(article1, article2, article3));
        this.mockMvc.perform(get("/api/articles").with(httpBasic("abc", "abc@123")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[*].title", isA(ArrayList.class)))
                .andExpect(jsonPath("$[*].title", contains("Title1", "Title2",
                        "Title3")));
    }

    @Test
    void should_list_all_articles_with_pagination() throws Exception {
        Article article1 = createArticle("Title1", "Description1", "body1");
        Article article2 = createArticle("Title2", "description2", "body2");
        Article article3 = createArticle("Title3", "description3", "body3");
        articleRepository.saveAll(Arrays.asList(article1, article2, article3));
        this.mockMvc.perform(get("/api/articles?page=0&size=1").with(httpBasic("abc",
                "abc@123")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[*].title", isA(ArrayList.class)))
                .andExpect(jsonPath("$[*].title", contains("Title1")));
    }

    private Article createArticle(String title, String description, String body) {
        Article article = new Article.Builder()
                .withTitle(title)
                .withDescription(description)
                .withBody(body)
                .withUpdatedAt()
                .build();
        article.setUser(user);
        return article;
    }


    @Test
    void should_delete_an_article() throws Exception {

        createAdminUser();
        Article article = new Article.Builder()
                .withTitle("spring")
                .withBody("appl")
                .withDescription("boot")
                .build();
        article.setUser(user);
        Article savedArticle = articleRepository.save(article);
        String id = String.format("%s-%s", savedArticle.getSlug(), savedArticle.getId());
        this.mockMvc.perform(delete("/api/articles/{slug_id}", id).with(httpBasic("admin"
                , "password"))
        ).andDo(print())
                .andExpect(status().isNoContent());
    }

    private void createAdminUser() {
        User admin = userRepository.findByUsernameOrEmail("admin", "admin@123.com");
        if (Objects.isNull(admin)) {
            admin = new User.Builder()
                    .withUsername("admin")
                    .withEmail("admin@123.com")
                    .withPassword(passwordEncoder.encode("password"))
                    .withUserRole(UserRole.ADMIN)
                    .build();
            userRepository.save(admin);
        }
    }

    @Test
    void should_not_delete_an_article() throws Exception {
        createAdminUser();
        String id = "abc" + "-" + UUID.randomUUID().toString();
        this.mockMvc.perform(delete("/api/articles/{slug_id}", id).with(httpBasic("admin"
                , "password"))
        ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void should_list_all_drafted_articles() throws Exception {
        Article article1 = createArticle("Title1", "Description1", "body1");
        Article article2 = createArticle("Title2", "description2", "body2");
        Article article3 = createArticle("Title3", "description3", "body3")
                .publish();
        articleRepository.saveAll(Arrays.asList(article1, article2, article3));
        this.mockMvc.perform(get("/api/articles?status=DRAFT").with(httpBasic("abc",
                "abc@123")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[*].title", isA(ArrayList.class)))
                .andExpect(jsonPath("$[*].title", contains("Title1", "Title2")));
    }


    @Test
    void should_publish_an_article() throws Exception {
        UserRequest userEditorRequest = new UserRequest.Builder()
                .withUsername("editor")
                .withPassword("editor@123")
                .withEmail("editor@123.com")
                .withUserRole(UserRole.EDITOR)
                .build();
        User editorUser = userEditorRequest.toUser(passwordEncoder);
        userRepository.save(editorUser);

        Article article = createArticle("Title", "Description", "body");
        Article saved = articleRepository.save(article);
        String slugId = String.format("%s-%s", saved.getSlug(), saved.getId());
        this.mockMvc.perform(post("/api/articles/{slug_id}/publish", slugId).with(httpBasic("editor", "editor@123")))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThat(articleRepository.findById(article.getId())
                .filter(a -> a.getStatus() == ArticleStatus.PUBLISH).isPresent()).isTrue();
    }

    @Test
    void should_give_bad_request_when_tried_to_publish_the_already_published_article() throws Exception {
        UserRequest userEditorRequest = new UserRequest.Builder()
                .withUsername("editor")
                .withPassword("editor@123")
                .withEmail("editor@123.com")
                .withUserRole(UserRole.EDITOR)
                .build();
        User editorUser = userEditorRequest.toUser(passwordEncoder);
        userRepository.save(editorUser);
        Article article = createArticle("Title", "Desc", "Body").publish();
        Article saved = articleRepository.save(article);
        String id = String.format("%s-%s", saved.getSlug(), saved.getId());
        this.mockMvc.perform(post("/api/articles/{slugUuid}/publish", id).with(httpBasic("editor", "editor@123")))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_give_read_time_of_an_article_when_i_provide_valid_data() throws Exception {
        Article article =
                createArticle("Title", "Desc", "You have to believe")
                        .publish();
        Article saved = articleRepository.save(article);
        String id = String.format("%s-%s", saved.getSlug(), saved.getId());
        this.mockMvc.perform(get("/api/articles/{slugUuid}/timetoread", id).with(httpBasic("abc", "abc@123")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articleId").isNotEmpty())
                .andExpect(jsonPath("$.articleId").value(id))
                .andExpect(jsonPath("$.readingTime").hasJsonPath())
                .andExpect(jsonPath("$..minutes").value(0))
                .andExpect(jsonPath("$..seconds").value(1));

    }

    @Test
    void should_get_tags_with_count_of_an_article_when_i_provide_valid_data() throws Exception {
        Article article =
                new Article.Builder().withBody("body")
                        .withTitle("title")
                        .withDescription("Desc")
                        .withTags(new HashSet<>(Arrays.asList("t1", "t2", "t3")))
                        .build();
        Article article2 =
                new Article.Builder().withBody("body")
                        .withTitle("title")
                        .withDescription("Desc")
                        .withTags(new HashSet<>(Collections.singletonList("t1")))
                        .build();
        article.setUser(user);
        article2.setUser(user);
        this.articleRepository.saveAll(Arrays.asList(article, article2));
        this.mockMvc.perform(get("/api/articles/tags").with(httpBasic("abc", "abc@123")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[*].tag").isArray())
                .andExpect(jsonPath("$[*].occurrence").isArray())
                .andExpect(jsonPath("$[*].tag", containsInAnyOrder("t1", "t2", "t3")))
                .andExpect(jsonPath("$[*].occurrence", containsInAnyOrder(2, 1, 1)));

    }

    @Test
    void should_favourite_an_article_when_i_provide_valid_data() throws Exception {
        Article article = createArticle("title", "desc", "body");
        Article saved = articleRepository.save(article);
        String id = String.format("%s-%s", saved.getSlug(), saved.getId());
        this.mockMvc.perform(put("/api/articles/{slug_id}/favourite", id).with(httpBasic("abc", "abc@123")))
                .andDo(print())
                .andExpect(status().isNoContent());
        Optional<Article> articleFavouritesCount =
                articleRepository.findById(article.getId());
        assertThat(articleFavouritesCount.map(Article::getFavouritesCount).isPresent()).isTrue();
        assertThat(articleFavouritesCount.map(Article::isFavourited).get()).isTrue();
        assertThat(articleFavouritesCount.get().getFavouritesCount()).isEqualTo(1L);

    }


    @Test
    void should_unfavourite_an_article_when_i_provide_valid_data() throws Exception {
        Article article = createArticle("title", "desc", "body");
        article.favourite();
        Article saved = articleRepository.save(article);
        String id = String.format("%s-%s", saved.getSlug(), saved.getId());
        this.mockMvc.perform(delete("/api/articles/{slug_id}/favourite", id).with(httpBasic("abc", "abc@123")))
                .andDo(print())
                .andExpect(status().isNoContent());
        Optional<Article> articleFavouritesCount =
                articleRepository.findById(article.getId());
        assertThat(articleFavouritesCount.map(Article::getFavouritesCount).isPresent()).isTrue();
        assertThat(articleFavouritesCount.map(Article::isFavourited).get()).isFalse();
        assertThat(articleFavouritesCount.get().getFavouritesCount()).isEqualTo(0L);

    }


    @Test
    void should_get_favourite_count_as_1_when_i_try_to_unfavourite_an_article_with_favourite_count_2() throws Exception {
        Article article = new Article.Builder()
                .withBody("body")
                .withDescription("desc")
                .withTitle("title")
                .withFavourited(true)
                .withFavouritesCount(2L)
                .build();
        article.setUser(user);
        Article saved = articleRepository.save(article);
        String id = String.format("%s-%s", saved.getSlug(), saved.getId());
        this.mockMvc.perform(delete("/api/articles/{slug_id}/favourite", id).with(httpBasic("abc", "abc@123")))
                .andDo(print())
                .andExpect(status().isNoContent());
        Optional<Article> articleFavouritesCount =
                articleRepository.findById(article.getId());
        assertThat(articleFavouritesCount.map(Article::getFavouritesCount).isPresent()).isTrue();
        assertThat(articleFavouritesCount.map(Article::isFavourited).get()).isTrue();
        assertThat(articleFavouritesCount.get().getFavouritesCount()).isEqualTo(1L);

    }

    @Disabled
    @Test
    void should_not_delete_an_article_when_the_user_is_not_owner_of_article() throws Exception {
        UserRequest userRequest1 =
                new UserRequest.Builder()
                        .withUsername("abc1").withPassword("abc1@123").withEmail("abc1" +
                        "@123.com").build();
        User user1 = userRequest1.toUser(passwordEncoder);
        userRepository.save(user1);
        Article article = new Article.Builder()
                .withTitle("spring")
                .withBody("appl")
                .withDescription("boot")
                .build();
        article.setUser(user);
        Article savedArticle = articleRepository.save(article);
        String id = String.format("%s-%s", savedArticle.getSlug(), savedArticle.getId());
        this.mockMvc.perform(delete("/api/articles/{slug_id}", id)
                .with(httpBasic("abc1", "abc1@123")))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void should_not_update_object_when_user_not_the_owner_of_same_article() throws Exception {
        ArticleRequest updateRequest = new ArticleRequest.Builder()
                .withBody("body")
                .withTitle("title")
                .withDescription("desc")
                .withTags(new HashSet<>(Arrays.asList("tags", "hello"))).build();
        Article article = new Article.Builder()
                .withTitle("DEF")
                .withDescription("Desc")
                .withBody("DEF")
                .build();
        article.setUser(user);
        Article saved = articleRepository.save(article);
        String json = objectMapper.writeValueAsString(updateRequest);
        String id = String.format("%s-%s", saved.getSlug(), saved.getId());
        UserRequest userRequest1 =
                new UserRequest.Builder()
                        .withUsername("abc1").withPassword("abc1@123").withEmail("abc1" +
                        "@123.com").build();
        User user1 = userRequest1.toUser(passwordEncoder);
        userRepository.save(user1);
        this.mockMvc.perform(patch("/api/articles/{slug_uuid}", id)
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .with(httpBasic("abc1", "abc1@123")))
                .andDo(print())
                .andExpect(status().isForbidden());
    }


    @Test
    void should_not_create_article_if_similar_article_is_already_registered() throws Exception {
        Article article = createArticle("title", "desc", "body");
        articleRepository.save(article);
        ArticleRequest articleRequest = new ArticleRequest.Builder().withBody(
                "body").withTitle("title").withDescription("desc").build();
        String json = objectMapper.writeValueAsString(articleRequest);
        this.mockMvc.perform(post("/api/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(httpBasic("abc", "abc@123")))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_update_article_if_similar_article_is_not_present_excluding_the_one_which_is_getting_updated() throws Exception {
        Article article = createArticle("title", "desc", "body");
        Article savedArticle = articleRepository.save(article);
        String id = String.format("%s-%s", savedArticle.getSlug(), savedArticle.getId());
        ArticleRequest articleRequest = new ArticleRequest.Builder().withBody(
                "body").withTitle("title").withDescription("desc").build();
        String json = objectMapper.writeValueAsString(articleRequest);
        this.mockMvc.perform(patch("/api/articles/{slug_id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(httpBasic("abc", "abc@123")))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    void should_not_update_article_if_similar_article_is_present_excluding_the_one_which_is_getting_updated() throws Exception {
        Article article = createArticle("title", "desc", "body");
        Article article1 = createArticle("spring", "desc", "spring");
        articleRepository.saveAll(Arrays.asList(article, article1));
        String id = String.format("%s-%s", article1.getSlug(), article1.getId());
        ArticleRequest articleRequest = new ArticleRequest.Builder().withBody(
                "body").withTitle("title").withDescription("desc").build();
        String json = objectMapper.writeValueAsString(articleRequest);
        this.mockMvc.perform(patch("/api/articles/{slug_id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(httpBasic("abc", "abc@123")))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void admin_should_be_able_to_create_article_when_mandatory_request_data_is_provided() throws Exception {

        createAdminUser();
        ArticleRequest articleRequest = new ArticleRequest.Builder()
                .withTitle("How to learn Spring Boot")
                .withBody("You have to believe")
                .withDescription("Ever wonder how?")
                .build();
        String json = objectMapper.writeValueAsString(articleRequest);

        mockMvc.perform(
                post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json).with(httpBasic("admin", "password")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value("How to learn Spring Boot"))
                .andExpect(jsonPath("$.slug").value("how-to-learn-spring-boot"))
                .andExpect(jsonPath("$.description").value("Ever wonder how?"))
                .andExpect(jsonPath("$.body").value("You have to believe"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.favourited").isBoolean())
                .andExpect(jsonPath("$.favourited").value(false))
                .andExpect(jsonPath("$.favouritesCount").value(0))
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andExpect(jsonPath("$.featuredImageUrl").isNotEmpty())
                .andExpect(jsonPath("$.featuredImageUrl").isString())
                .andExpect(jsonPath("$.featuredImageUrl").value("test image"))
                .andExpect(jsonPath("$.author").hasJsonPath())
                .andExpect(jsonPath("$..username").value("admin"));
    }

    @Test
    void admin_should_be_able_to_publish_an_article() throws Exception {
        createAdminUser();
        Article article = createArticle("Title", "Description", "body");
        Article saved = articleRepository.save(article);
        String slugId = String.format("%s-%s", saved.getSlug(), saved.getId());
        this.mockMvc.perform(post("/api/articles/{slug_id}/publish", slugId).with(httpBasic("admin", "password")))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThat(articleRepository.findById(article.getId())
                .filter(a -> a.getStatus() == ArticleStatus.PUBLISH).isPresent()).isTrue();
    }


}