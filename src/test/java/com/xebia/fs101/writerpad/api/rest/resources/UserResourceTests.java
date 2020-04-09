package com.xebia.fs101.writerpad.api.rest.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.fs101.writerpad.api.rest.representations.UserRequest;
import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.domain.User;
import com.xebia.fs101.writerpad.domain.UserRole;
import com.xebia.fs101.writerpad.repositories.ArticleRepository;
import com.xebia.fs101.writerpad.repositories.CommentRepository;
import com.xebia.fs101.writerpad.repositories.UserRepository;
import com.xebia.fs101.writerpad.services.security.CustomUserDetails;
import com.xebia.fs101.writerpad.services.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    private String accessToken;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
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
        accessToken = jwtTokenProvider.generateToken(new CustomUserDetails(admin));
    }


    @AfterEach
    void tearDown() {
        this.commentRepository.deleteAll();
        this.articleRepository.deleteAll();
        this.userRepository.deleteAll();
    }


    @Test
    void should_create_user() throws Exception {

        UserRequest userRequest = new UserRequest.Builder()
                .withEmail("abc@123.com").withPassword("abc@123").withUsername("abc")
                .build();

        String json = objectMapper.writeValueAsString(userRequest);
        mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json).header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userid").isNotEmpty())
                .andExpect(jsonPath("$.username").isNotEmpty())
                .andExpect(jsonPath("$.email").isNotEmpty())
                .andExpect(jsonPath("$.username").value("abc"))
                .andExpect(jsonPath("$.email").value("abc@123.com"))
                .andExpect(jsonPath("$.following").value(false));
    }


    @Test
    void should_give_bad_request_when_i_try_to_save_same_user_again() throws Exception {

        User user = new User.Builder()
                .withEmail("abc@123.com").withPassword(passwordEncoder.encode("abc@123"))
                .withUsername("abc").build();

        this.userRepository.save(user);

        UserRequest userRequest = new UserRequest.Builder()
                .withEmail("abc@123.com").withPassword("abc@123").withUsername("abc").build();

        String json = objectMapper.writeValueAsString(userRequest);

        mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json).header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }


    @Test
    void should_be_able_to_return_a_user_profile() throws Exception {

        User user = new User.Builder()
                .withEmail("abc@123.com").withPassword(passwordEncoder.encode("abc@123")).withUsername("abc").build();
        User savedUser = this.userRepository.save(user);
        Article article = new Article.Builder().withTitle("title").withDescription(
                "desc").withBody("body").build();
        article.setUser(user);
        article.publish();
        articleRepository.save(article);
        this.mockMvc.perform(get("/api/profiles/{username}", savedUser.getUsername()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("abc"))
                .andExpect(jsonPath("$.articles").hasJsonPath())
                .andExpect(jsonPath("$..id").value(article.getId().toString()))
                .andExpect(jsonPath("$..title").value("title"))
                .andExpect(jsonPath("$.following").value(false));
    }


    @Test
    void should_be_able_to_return_a_user_profile_with_following_true_if_login_user_is_following() throws Exception {

        User user = new User.Builder()
                .withEmail("abc@123.com").withPassword(passwordEncoder.encode("abc@123")).withUsername("abc").build();
        User savedUser = this.userRepository.save(user);
        Article article = new Article.Builder().withTitle("title").withDescription(
                "desc").withBody("body").build();
        article.setUser(user);
        article.publish();
        articleRepository.save(article);

        User user2 = new User.Builder()
                .withEmail("efg@123.com").withPassword(passwordEncoder.encode("efg@123"))
                .withUsername("efg").build();
        user2.follow();
        User savedUser2 = this.userRepository.save(user2);
        savedUser.addFollowers(user2.getUsername());
        userRepository.save(user);

        String user2AccessToken = this.jwtTokenProvider.generateToken(new CustomUserDetails(user2));
        this.mockMvc.perform(get("/api/profiles/{username}", savedUser.getUsername())
                .header("Authorization", "Bearer " + user2AccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("abc"))
                .andExpect(jsonPath("$.articles").hasJsonPath())
                .andExpect(jsonPath("$..id").value(article.getId().toString()))
                .andExpect(jsonPath("$..title").value("title"))
                .andExpect(jsonPath("$.following").value(true));

    }


    @Test
    void should_be_able_to_follow_an_user() throws Exception {
        User user1 = new User.Builder()
                .withEmail("abc@123.com").withPassword(passwordEncoder.encode("abc@123")).withUsername("abc")
                .withFollowingCount(9).build();
        User user2 = new User.Builder()
                .withEmail("efg@123.com").withPassword(passwordEncoder.encode("efg@123"))
                .withUsername("efg").build();
        userRepository.saveAll(Arrays.asList(user1, user2));

        String user1AccessToken = this.jwtTokenProvider.generateToken(new CustomUserDetails(user1));
        this.mockMvc.perform(post("/api/profiles/{username}/follow", user2.getUsername())
                .header("Authorization", "Bearer " + user1AccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.followerCount").value(1))
                .andExpect(jsonPath("$.following").value(true));
        User abc = userRepository.findByUsernameOrEmail("abc", null);
        assertThat(abc.getFollowingCount()).isEqualTo(10);

    }

    @Test
    void should_be_able_to_unFollow_an_user() throws Exception {
        User user1 = new User.Builder()
                .withEmail("abc@123.com").withPassword(passwordEncoder.encode("abc@123")).withUsername("abc")
                .withFollowingCount(1).build();
        User user2 = new User.Builder()
                .withEmail("efg@123.com").withPassword(passwordEncoder.encode("efg@123"))
                .withFollowerCount(9).withUsername("efg").build();
        user2.addFollowers(user1.getUsername());
        userRepository.saveAll(Arrays.asList(user1, user2));
        String user1AccessToken = this.jwtTokenProvider.generateToken(new CustomUserDetails(user1));

        this.mockMvc.perform(delete("/api/profiles/{username}/unfollow",
                user2.getUsername())
                .header("Authorization", "Bearer " + user1AccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.followerCount").value(9))
                .andExpect(jsonPath("$.following").value(false));
        User abc = userRepository.findByUsernameOrEmail("abc", null);
        assertThat(abc.getFollowingCount()).isEqualTo(0);
        assertThat(abc.isFollowing()).isFalse();

    }


}