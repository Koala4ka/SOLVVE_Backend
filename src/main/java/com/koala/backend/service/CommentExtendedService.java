package com.koala.backend.service;

import com.koala.backend.domain.Comment;
import com.koala.backend.dto.comment.CommentReadExtendedDTO;
import com.koala.backend.exception.EntityNotFoundException;
import com.koala.backend.repository.CommentExtendedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CommentExtendedService {

    @Autowired
    private CommentExtendedRepository commentExtendedRepository;

    @Autowired
    private TranslationService translationService;


    public CommentReadExtendedDTO getCommentExtended(UUID id) {
        Comment comment = getCommentRequired(id);
        return translationService.toReadExtended(comment);
    }

    private Comment getCommentRequired(UUID id) {
        return commentExtendedRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(Comment.class, id)
        );
    }

}
