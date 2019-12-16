package com.xebia.fs101.writerpad.api.rest.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.fs101.writerpad.api.rest.representations.UserRequest;
import com.xebia.fs101.writerpad.domain.User;
import com.xebia.fs101.writerpad.domain.UserRole;
import com.xebia.fs101.writerpad.repositories.ArticleRepository;
import com.xebia.fs101.writerpad.repositories.CommentRepository;
import com.xebia.fs101.writerpad.repositories.UserRepository;
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
import java.util.List;
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
                        .content(json).with(httpBasic("admin", "password")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userid").isNotEmpty())
                .andExpect(jsonPath("$.username").isNotEmpty())
                .andExpect(jsonPath("$.email").isNotEmpty())
                .andExpect(jsonPath("$.username").value("abc"))
                .andExpect(jsonPath("$.email").value("abc@123.com"));
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
                        .content(json).with(httpBasic("admin", "password")))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }


    @Test
    void should_be_able_to_return_a_user_profile() throws Exception {

        User user = new User.Builder()
                .withEmail("abc@123.com").withPassword(passwordEncoder.encode("abc@123")).withUsername("abc").build();
        User savedUser = this.userRepository.save(user);
        this.mockMvc.perform(get("/api/profiles/{username}", savedUser.getUsername()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("abc"));
    }

    @Test
    void should_be_able_to_follow_an_user() throws Exception {
        User user1 = new User.Builder()
                .withEmail("abc@123.com").withPassword(passwordEncoder.encode("abc@123")).withUsername("abc").build();
        User user2 =  new User.Builder()
                .withEmail("efg@123.com").withPassword(passwordEncoder.encode("efg@123")).withUsername("efg").build();
        userRepository.saveAll(Arrays.asList(user1, user2));
        this.mockMvc.perform(post("/api/profiles/{username}/follow", user2.getUsername())
                .with(httpBasic("abc", "abc@123")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.followerCount").value(1));
        User abc = userRepository.findByUsernameOrEmail("abc",null);
        assertThat(abc.getFollowingCount()).isEqualTo(1);
        assertThat(abc.isFollowing()).isTrue();
    }

    @Test
    void should_be_able_to_unFollow_an_user() throws Exception {
        User user1 = new User.Builder()
                .withEmail("abc@123.com").withPassword(passwordEncoder.encode("abc@123")).withUsername("abc").build();
        User user2 =  new User.Builder()
                .withEmail("efg@123.com").withPassword(passwordEncoder.encode("efg@123")).withUsername("efg").build();
        userRepository.saveAll(Arrays.asList(user1, user2));
        userRepository.save(user1);
        userRepository.save(user2);

        this.mockMvc.perform(delete("/api/profiles/{username}/unfollow", user2.getUsername())
                .with(httpBasic("abc", "abc@123")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.followingCount").value(0))
                .andExpect(jsonPath("$.following").value(false));


    }


}