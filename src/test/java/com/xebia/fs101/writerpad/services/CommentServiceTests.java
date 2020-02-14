package com.xebia.fs101.writerpad.services;

import com.xebia.fs101.writerpad.domain.Comment;
import com.xebia.fs101.writerpad.repositories.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTests {

    @InjectMocks
    private CommentService commentService;
    @Mock
    private CommentRepository commentRepository;

    @Test
    void should_save_the_comment() {
        when(commentRepository.save(any())).thenReturn(new Comment());
        Comment comment = new Comment();
        commentService.save(comment);
        verify(commentRepository).save(comment);
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void should_be_able_to_get_all_comments_for_given_article_id() {
        when(commentRepository.findAllByArticleId(any()))
                .thenReturn(Collections.singletonList(new Comment()));
        UUID uuid = UUID.randomUUID();
        commentService.findAllByArticleId(uuid);
        verify(commentRepository).findAllByArticleId(uuid);
        verifyNoMoreInteractions(commentRepository);
    }


    @Test
    void should_be_able_to_delete_a_comment() {
        when(commentRepository.findById(any())).thenReturn(Optional.of(new Comment()));
        commentService.delete(1L);
        verify(commentRepository, times(1)).findById(1L);
        verify(commentRepository).deleteById(1L);
        verifyNoMoreInteractions(commentRepository);
    }


}