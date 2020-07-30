package com.koala.backend.repository;

import com.koala.backend.domain.Comment;
import com.koala.backend.dto.comment.CommentFilter;

import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> findByFilter(CommentFilter filter);
}
