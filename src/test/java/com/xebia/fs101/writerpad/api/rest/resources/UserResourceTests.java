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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
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
                .withEmail("abc@123.com").withPassword("abc@123").withUsername("abc").build();

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


}