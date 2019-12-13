package com.xebia.fs101.writerpad;

import com.xebia.fs101.writerpad.repositories.ArticleRepository;
import com.xebia.fs101.writerpad.repositories.CommentRepository;
import com.xebia.fs101.writerpad.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class WriterpadApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @AfterEach
    void tearDown() {
        this.commentRepository.deleteAll();
        this.articleRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    void contextLoads() {
    }


}
