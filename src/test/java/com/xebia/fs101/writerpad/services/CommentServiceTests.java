package com.xebia.fs101.writerpad.services;

import com.xebia.fs101.writerpad.domain.Comment;
import com.xebia.fs101.writerpad.repositories.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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
        Comment comment = new Comment();
        commentService.save(comment);
        verify(commentRepository).save(comment);
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void should_be_able_to_delete_a_comment() {
        long id = 1;
        when(commentRepository.findById(id)).thenReturn(Optional.of(new Comment()));
        commentService.delete(id);
        verify(commentRepository, times(1)).findById(id);
        verify(commentRepository).deleteById(id);
        verifyNoMoreInteractions(commentRepository);
    }

}