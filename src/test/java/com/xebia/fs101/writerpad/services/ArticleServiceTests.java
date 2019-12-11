package com.xebia.fs101.writerpad.services;

import com.xebia.fs101.writerpad.api.rest.representations.UserRequest;
import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.domain.ArticleStatus;
import com.xebia.fs101.writerpad.domain.User;
import com.xebia.fs101.writerpad.exceptions.ForbiddenOperationException;
import com.xebia.fs101.writerpad.repositories.ArticleRepository;
import com.xebia.fs101.writerpad.repositories.UserRepository;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTests {

    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ArticleService articleService;




    @Test
    void should_save_the_article() {
        User user = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(articleRepository.save(any())).thenReturn(new Article());
        Article article = new Article.Builder()
                .withBody("body")
                .withTitle("title")
                .withDescription("desc").build();
        article.setUser(user);
        articleService.save(article, user);
        verify(userRepository).findById(user.getUserid());
        verify(articleRepository).save(article);
        Article articleWithStatusDraft =
                new Article.Builder().withBody("body")
                        .withTitle("title")
                        .withDescription("desc")
                        .withStatus(ArticleStatus.DRAFT)
                        .build();
        articleWithStatusDraft.setUser(user);
        verify(articleRepository).save(refEq(articleWithStatusDraft, "createdAt"));
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
    void should_be_able_to_update_an_article() {
        User user = new User();
        Article article = new Article.Builder()
                .withBody("body")
                .withDescription("desc")
                .withTitle("title")
                .build();
        article.setUser(user);
        when(articleRepository.findById(any())).thenReturn(Optional.of(article));
        when(articleRepository.save(any())).thenReturn(new Article());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        UUID id = UUID.randomUUID();
        Article articleTobeUpdated = new Article.Builder()
                .withBody("updated body")
                .build();
        articleService.update("slug-" + id, articleTobeUpdated,user);
        verify(articleRepository).findById(id);
        Article expectedArticle =
                new Article.Builder().withBody("updated body")
                        .withDescription("desc")
                        .withTitle("title")
                        .build();
        expectedArticle.setUser(user);
        verify(userRepository).findById(user.getUserid());
        verify(articleRepository).save(refEq(expectedArticle, "createdAt",
                "updatedAt"));
        verifyNoMoreInteractions(articleRepository);
    }

    @Test
    void should_give_all_the_articles() {
        when(articleRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(new Article())));
        PageRequest pageable = PageRequest.of(0, 3);
        this.articleService.findAll(null, pageable);
        verify(articleRepository).findAll(pageable);
        verifyNoMoreInteractions(articleRepository);
    }

    @Test
    void should_be_able_to_delete_a_article() {
        User user = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        Article article = new Article();
        article.setUser(user);
        when(articleRepository.findById(any())).thenReturn(Optional.of(article));
        UUID id = UUID.randomUUID();
        this.articleService.delete("slug-" + id,user);
        verify(articleRepository, times(1)).findById(id);
        verify(userRepository).findById(user.getUserid());
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
        boolean publish = articleService.publish("slug-" + id);
        assertThat(publish).isTrue();
        verify(articleRepository, times(1)).findById(id);
        Article expectedArticle =
                new Article.Builder().withBody("body")
                        .withDescription("desc")
                        .withTitle("title")
                        .withStatus(ArticleStatus.PUBLISH)
                        .build();
        verify(articleRepository).save(refEq(expectedArticle, "createdAt"));
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
        boolean publish = articleService.publish("slug-" + id);
        assertThat(publish).isFalse();
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
    void should_favourite_an_article_when_i_provide_valid_data() {
        Optional<Article> optionalArticle = Optional.of(new Article.Builder()
                .withFavourited(false).withFavouritesCount(0L).build());
        when(articleRepository.findById(any())).thenReturn(optionalArticle);

        UUID uuid = UUID.randomUUID();
        this.articleService.favourite("slug-" + uuid);
        verify(articleRepository, times(1)).findById(uuid);
        Article expectedArticle =
                new Article.Builder().withFavourited(true).withFavouritesCount(1L).build();
        verify(articleRepository, times(1)).save(refEq(expectedArticle,
                "createdAt"));
        verifyNoMoreInteractions(articleRepository);

    }


    @Test
    void should_unfavourite_an_article_when_i_provide_valid_data()  {
        Optional<Article> optionalArticle = Optional.of(new Article.Builder()
                .withFavourited(true).withFavouritesCount(1L).build());
        when(articleRepository.findById(any())).thenReturn(optionalArticle);

        UUID uuid = UUID.randomUUID();
        this.articleService.unFavourite("slug-" + uuid);
        verify(articleRepository, times(1)).findById(uuid);
        Article expectedArticle =
                new Article.Builder().withFavourited(false).withFavouritesCount(0L).build();
        verify(articleRepository, times(1)).save(refEq(expectedArticle,
                "createdAt"));
        verifyNoMoreInteractions(articleRepository);

    }


    @Test
    void should_get_favourite_count_as_1_when_i_try_to_unfavourite_an_article_with_favourite_count_2() {
        Optional<Article> optionalArticle = Optional.of(new Article.Builder()
                .withFavourited(true).withFavouritesCount(2L).build());
        when(articleRepository.findById(any())).thenReturn(optionalArticle);
        UUID uuid = UUID.randomUUID();
        this.articleService.unFavourite("slug-" + uuid);
        verify(articleRepository, times(1)).findById(uuid);
        Article expectedArticle =
                new Article.Builder().withFavourited(true).withFavouritesCount(1L).build();
        verify(articleRepository, times(1)).save(refEq(expectedArticle,
                "createdAt"));
        verifyNoMoreInteractions(articleRepository);

    }

    @Test
    void should_not_update_the_article_if_user_is_not_owner() {
        User user1=new User.Builder().withUsername("test").build();
        User user = new User();
        Article article = new Article.Builder()
                .withBody("body")
                .withDescription("desc")
                .withTitle("title")
                .build();
        article.setUser(user);
        when(articleRepository.findById(any())).thenReturn(Optional.of(article));
        when(userRepository.findById(any())).thenReturn(Optional.of(user1));
        UUID id = UUID.randomUUID();
        Article articleTobeUpdated = new Article.Builder()
                .withBody("updated body")
                .build();
        articleTobeUpdated.setUser(user);

        ForbiddenOperationException thrown =
                assertThrows(ForbiddenOperationException.class,
                        () ->  articleService.update("slug-" + id, articleTobeUpdated, user),
                        "Expected doThing() to throw, but it didn't");
        assertTrue(thrown.getMessage().contains("You are not allowed to perform this operation"));
        verify(articleRepository).findById(id);
        verify(userRepository).findById(user.getUserid());
        verifyNoMoreInteractions(articleRepository);

    }


    @Test
    void should_not_delete_the_article_if_user_is_not_owner() {
        User user1=new User.Builder().withUsername("test").build();
        User user = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        Article article = new Article();
        article.setUser(user);
        when(articleRepository.findById(any())).thenReturn(Optional.of(article));
        UUID id = UUID.randomUUID();
        ForbiddenOperationException thrown =
                assertThrows(ForbiddenOperationException.class,
                        () ->  articleService.delete("slug-" + id, user),
                        "Expected doThing() to throw, but it didn't");
        assertTrue(thrown.getMessage().contains("You are not allowed to perform this operation"));
        verify(articleRepository).findById(id);
        verify(userRepository).findById(user.getUserid());
        verifyNoMoreInteractions(articleRepository);


    }


}