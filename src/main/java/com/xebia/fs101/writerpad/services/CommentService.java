package com.xebia.fs101.writerpad.services;

import com.xebia.fs101.writerpad.domain.Comment;
import com.xebia.fs101.writerpad.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment save(Comment comment) {
        return this.commentRepository.save(comment);
    }

    public List<Comment> findAllByArticleId(UUID uuid) {
        return this.commentRepository.findAllByArticleId(uuid);
    }

    public boolean delete(Long id) {
        Optional<Comment> optionalComment = this.commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            this.commentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
