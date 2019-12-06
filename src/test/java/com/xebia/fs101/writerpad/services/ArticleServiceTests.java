package com.xebia.fs101.writerpad.services;

import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.repositories.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTests {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleService articleService;


    @Test
    void should_save_the_article() {
        when(articleRepository.save(any())).thenReturn(new Article());
        Article article = new Article.Builder()
                .withBody("body")
                .withTitle("title")
                .withDescription("desc").build();
        articleService.save(article);
        verify(articleRepository).save(article);
        verifyNoMoreInteractions(articleRepository);
    }

    @Test
    void should_be_able_to_find_an_article() {
        when(articleRepository.findById(any())).thenReturn(Optional.of(new Article()));
        UUID id = UUID.randomUUID();
        articleService.findById("slug-" + id);
        verify(articleRepository).findById(id);
        verifyNoMoreInteractions(articleRepository);
    }

    @Test
    public void should_be_able_to_update_an_article() {
        Article article = new Article.Builder()
                .withBody("body")
                .withDescription("desc")
                .withTitle("title")
                .build();
        when(articleRepository.findById(any())).thenReturn(Optional.of(article));
        when(articleRepository.save(any())).thenReturn(new Article());
        UUID id = UUID.randomUUID();
        Article articleTobeUpdated = new Article.Builder()
                .withBody("updated body")
                .build();
        articleService.update("slug-" + id, articleTobeUpdated);
        verify(articleRepository).findById(id);
        verify(articleRepository).save(article);
        verifyNoMoreInteractions(articleRepository);
    }

    @Test
    void should_give_all_the_articles() {
        Pageable pageable = PageRequest.of(0, 3);
        when(articleRepository.findAll(pageable))
                .thenReturn(new PageImpl<Article>(Collections.singletonList(new Article())));
        articleService.findAll(null, pageable);
        verify(articleRepository).findAll(pageable);
        verifyNoMoreInteractions(articleRepository);
    }

    @Test
    void should_be_able_to_delete_a_article() {
        when(articleRepository.findById(any())).thenReturn(Optional.of(new Article()));
        UUID id = UUID.randomUUID();
        articleService.delete("slug-" + id);
        verify(articleRepository, times(1)).findById(id);
        verify(articleRepository).deleteById(id);
        verifyNoMoreInteractions(articleRepository);
    }

    @Test
    void should_publish_an_article_when_valid_data_is_provided() {
        Article article = new Article.Builder()
                .withBody("body")
                .withDescription("desc")
                .withTitle("title")
                .build();
        when(articleRepository.findById(any())).thenReturn(Optional.of(article));
        UUID id = UUID.randomUUID();
        articleService.publish("slug-" + id);
        verify(articleRepository, times(1)).findById(id);
        verify(articleRepository).save(article);
        verifyNoMoreInteractions(articleRepository);
    }

    @Test
    void should_not_publish_an_article_when_an_article_is_already_published() {
        Article article = new Article.Builder()
                .withBody("body")
                .withDescription("desc")
                .withTitle("title")
                .build()
                .publish();
        when(articleRepository.findById(any())).thenReturn(Optional.of(article));
        UUID id = UUID.randomUUID();
        articleService.publish("slug-" + id);
        verify(articleRepository, times(1)).findById(id);
        verifyNoMoreInteractions(articleRepository);
    }


    @Test
    void should_get_tags_with_count_of_an_article_when_i_provide_valid_data() {

        when(articleRepository.findAllTags()).thenReturn(Stream.of("t1", "t2", "t3", "t1",
                "t2"));

        Map<String, Long> tagsWithCount = articleService.getTagsWithCount();
        assertThat(tagsWithCount).containsOnly(entry("t1", 2L), entry("t2", 2L), entry(
                "t3"
                , 1L));
        verify(articleRepository, times(1)).findAllTags();
        verifyNoMoreInteractions(articleRepository);
    }


    @Test
    void should_favourite_an_article_when_i_provide_valid_data() throws Exception {
        Optional<Article> optionalArticle = Optional.of(new Article.Builder()
                .withFavorited(true).withFavoritesCount(1L).build());
        when(articleRepository.findById(any())).thenReturn(optionalArticle);

        UUID uuid = UUID.randomUUID();
        this.articleService.favourite("slug-" + uuid);
        verify(articleRepository, times(1)).findById(uuid);
        verify(articleRepository, times(1)).save(optionalArticle.get());
        verifyNoMoreInteractions(articleRepository);

    }


    @Test
    void should_unfavourite_an_article_when_i_provide_valid_data() throws Exception {
        Optional<Article> optionalArticle = Optional.of(new Article.Builder()
                .withFavorited(false).withFavoritesCount(0L).build());
        when(articleRepository.findById(any())).thenReturn(optionalArticle);

        UUID uuid = UUID.randomUUID();
        this.articleService.unFavourite("slug-" + uuid);
        verify(articleRepository, times(1)).findById(uuid);
        verify(articleRepository, times(1)).save(optionalArticle.get());
        verifyNoMoreInteractions(articleRepository);

    }


    @Test
    void should_get_favourite_count_as_1_when_favourites_count_is_2() throws Exception {
        Optional<Article> optionalArticle = Optional.of(new Article.Builder()
                .withFavorited(true).withFavoritesCount(2L).build());
        when(articleRepository.findById(any())).thenReturn(optionalArticle);

        UUID uuid = UUID.randomUUID();
        this.articleService.favourite("slug-" + uuid);
        verify(articleRepository, times(1)).findById(uuid);
        verify(articleRepository, times(1)).save(optionalArticle.get());
        verifyNoMoreInteractions(articleRepository);

    }

}