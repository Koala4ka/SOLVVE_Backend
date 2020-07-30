package com.koala.backend.service;

import com.koala.backend.domain.Comment;
import com.koala.backend.dto.comment.*;
import com.koala.backend.exception.EntityNotFoundException;
import com.koala.backend.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private PortalUserService portalUserService;

    @Autowired
    private FilmService filmService;


    public CommentReadDTO getComment(UUID id) {

        Comment comment = getCommentRequired(id);

        return translationService.toRead(comment);
    }

    public List<CommentReadDTO> getComments(CommentFilter filter) {

        List<Comment> comments = commentRepository.findByFilter(filter);
        return comments.stream().map(translationService::toRead).collect(Collectors.toList());
    }

    public CommentReadDTO createComment(CommentCreateDTO create) {

        Comment comment = translationService.toEntity(create);
        comment = commentRepository.save(comment);

        return translationService.toRead(comment);
    }

    public CommentReadDTO patchComment(UUID id, CommentPatchDTO patch) {

        Comment comment = getCommentRequired(id);

        translationService.patchEntity(patch, comment);

        comment = commentRepository.save(comment);

        return translationService.toRead(comment);
    }

    public CommentReadDTO updateComment(UUID id, CommentPutDTO update) {

        Comment comment = getCommentRequired(id);

        comment.setDateAt(update.getDateAt());
        comment.setStatus(update.getStatus());
        comment.setPortalUser(portalUserService.getPortalUserRequired(update.getPortalUserId()));
        comment.setFilm(filmService.getFilmRequired(update.getFilmId()));

        comment = commentRepository.save(comment);

        return translationService.toRead(comment);
    }

    public void deleteComment(UUID id) {
        commentRepository.delete(getCommentRequired(id));
    }

    private Comment getCommentRequired(UUID id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(Comment.class, id)
        );
    }

}


