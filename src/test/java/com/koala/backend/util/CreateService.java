package com.koala.backend.util;

import com.koala.backend.domain.Comment;
import com.koala.backend.domain.CommentStatus;
import com.koala.backend.domain.Film;
import com.koala.backend.domain.PortalUser;
import com.koala.backend.repository.CommentRepository;
import com.koala.backend.repository.FilmRepository;
import com.koala.backend.repository.PortalUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class CreateService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private FilmRepository filmRepository;

    public Comment createComment() {

        Comment comment = new Comment();

        comment.setId(UUID.randomUUID());
        comment.setDateAt(null);
        comment.setPortalUserMark(null);
        comment.setStatus(CommentStatus.IN_CHECK);
        comment.setPortalUser(createPortalUser());
        comment.setFilm(createFilm());

        return commentRepository.save(comment);
    }

    public PortalUser createPortalUser() {

        PortalUser portalUser = new PortalUser();

        portalUser.setId(UUID.randomUUID());
        portalUser.setName("Tramp");
        portalUser.setCountry("123");
        portalUser.setAge(25);

        return portalUserRepository.save(portalUser);
    }

    public Film createFilm() {

        Film film = new Film();

        Comment comment = new Comment();

        film.setId(UUID.randomUUID());
        film.setFilmName("Jumanji: Welcome to the Jungle");
        film.setFilmYear(2019);
        film.setFilmCountry("USA");
        film.setFilmScenario("Chris McKenna");
        film.setFilmBudget((long) 150.000000);
        film.setFilmDuration((int) 1.60);

        return filmRepository.save(film);
    }

    public Comment createComment(PortalUser portalUser, Film film) {

        Comment comment = new Comment();

        comment.setId(UUID.randomUUID());
        comment.setDateAt(null);
        comment.setPortalUserMark(4.5);
        comment.setStatus(CommentStatus.CHECKED);
        comment.setPortalUser(portalUser);
        comment.setFilm(film);

        return commentRepository.save(comment);
    }

    public Comment createComment(PortalUser portalUser, Film film, CommentStatus commentStatus) {

        Comment comment = new Comment();

        comment.setId(UUID.randomUUID());
        comment.setDateAt(null);
        comment.setPortalUserMark(4.5);
        comment.setStatus(commentStatus);
        comment.setPortalUser(portalUser);
        comment.setFilm(film);

        return commentRepository.save(comment);
    }

    public Comment createComment(PortalUser portalUser, Film film, CommentStatus commentStatus, Instant instant) {

        Comment comment = new Comment();

        comment.setId(UUID.randomUUID());
        comment.setDateAt(instant);
        comment.setPortalUserMark(4.5);
        comment.setStatus(commentStatus);
        comment.setPortalUser(portalUser);
        comment.setFilm(film);

        return commentRepository.save(comment);
    }

    public Comment createComment(PortalUser portalUser, Film film, Integer i) {

        Comment comment = new Comment();

        comment.setId(UUID.randomUUID());
        comment.setPortalUserMark(4.5);
        comment.setDateAt(null);
        comment.setStatus(CommentStatus.CHECKED);
        comment.setPortalUser(portalUser);
        comment.setFilm(film);

        return commentRepository.save(comment);
    }


    public Instant createInstant(int hour) {
        return LocalDateTime.of(2019, 12, 23, hour, 0).toInstant(ZoneOffset.UTC);
    }

}
