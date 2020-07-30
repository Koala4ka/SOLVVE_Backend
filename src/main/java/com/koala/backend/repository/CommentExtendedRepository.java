package com.koala.backend.repository;

import com.koala.backend.domain.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentExtendedRepository extends CrudRepository<Comment, UUID> {
}