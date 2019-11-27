package com.xebia.fs101.writerpad.repositories;

import com.xebia.fs101.writerpad.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByArticleId(UUID uuid);
}
