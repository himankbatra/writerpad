package com.xebia.fs101.writerpad.services;

import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.repositories.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTests {


    @InjectMocks
    ArticleService articleService;
    @Mock
    ArticleRepository articleRepository;

    @Test
    void should_save_the_article() {
        Article article = new Article.Builder().withBody("body").build();
        articleService.save(article);
        verify(articleRepository).save(article);
        verifyNoMoreInteractions(articleRepository);
    }

    @Test
    void should_be_able_to_delete_a_article() {
        UUID id = UUID.randomUUID();
        when(articleRepository.findById(id)).thenReturn(Optional.of(new Article()));
        articleService.delete("slug-" + id);
        verify(articleRepository, times(1)).findById(id);
        verify(articleRepository).deleteById(id);
        verifyNoMoreInteractions(articleRepository);
    }
}