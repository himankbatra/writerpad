package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.api.rest.representations.ArticleRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class ArticleServiceTests {


    @Autowired
    private ArticleService articleService;

    @Test
    void should_be_able_to_save_valid_article() throws Exception {

        ArticleRequest articleRequest = new ArticleRequest.Builder().
                withTitle("junit")
                .withDescription("used for testing")
                .withBody("this is body for test case")
                .withTags(new HashSet<String>(Collections.singletonList("abc,def"))).
                        build();

        Article savedArticle = articleService.save(articleRequest.toArticle());
        assertThat(savedArticle).isNotNull();
    }

}