package com.xebia.fs101.writerpad.api.rest.resources;


import com.xebia.fs101.writerpad.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ArticleResourceTests {

    @SpyBean
    private ArticleService articleService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void mock_mvc_should_not_be_null() {
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void when_i_pass_article_request_with_all_field_should_get_response_created() throws Exception {
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
    }

    @Test
    void when_i_pass_article_request_title_blank_should_give_status_bad_request() throws Exception {
        String json = "{\n" +
                "  \"title\": ,\n" +
                "  \"description\": \"Ever wonder how?\",\n" +
                "  \"body\": \"You have to believe\",\n" +
                "  \"tags\": [\"java\", \"Spring Boot\", \"tutorial\"],\n" +
                "  \"featuredImageUrl\": \"url of the featured image\"\n" +
                "}";
        mockMvc.perform(post("/api/articles")
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
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


/*    @Test
    public void should_patch() throws Exception {


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

    json = "{\n" +
                "    \"title\": \"How to learn Spring Boot by building an app\"\n" +
                "}";

        mockMvc.perform( patch( "how-to-learn-spring-boot-4c317b1e-8c51-4a32-8f99-9f4cdf90515d" )
                .contentType( MediaType.APPLICATION_JSON_VALUE )
                .content( json )
        ).andExpect( status().isOk() );
    }
    @Test
    public void should_find() throws Exception {
        mockMvc.perform( get( "/api/articles/how-dns-works-aa2b1302-7e8d-43d0-9686-eddd712b495f" )
        ).andExpect( status().isOk() );
    }
    @Test
    public void should_delete() throws Exception {
        mockMvc.perform( delete( "/api/articles/how-dns-works-aa2b1302-7e8d-43d0-9686-eddd712b495f" )
        ).andExpect( status().isOk() );
    }
    @Test
    public void should_get_all_articles() throws Exception {
        mockMvc.perform( get( "/api/articles" )
        ).andExpect( status().isOk() );
    }*/

}