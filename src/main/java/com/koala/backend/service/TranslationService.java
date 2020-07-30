package com.koala.backend.service;

import com.koala.backend.domain.*;
import com.koala.backend.dto.actorrole.ActorRoleCreateDTO;
import com.koala.backend.dto.actorrole.ActorRoleReadDTO;
import com.koala.backend.dto.comment.CommentCreateDTO;
import com.koala.backend.dto.comment.CommentPatchDTO;
import com.koala.backend.dto.comment.CommentReadDTO;
import com.koala.backend.dto.comment.CommentReadExtendedDTO;
import com.koala.backend.dto.film.FilmReadDTO;
import com.koala.backend.dto.like.LikePatchDTO;
import com.koala.backend.dto.like.LikeReadDTO;
import com.koala.backend.dto.portaluser.PortalUserCreateDTO;
import com.koala.backend.dto.portaluser.PortalUserPatchDTO;
import com.koala.backend.dto.portaluser.PortalUserReadDTO;
import com.koala.backend.repository.FilmRepository;
import com.koala.backend.repository.PortalUserRepository;
import com.koala.backend.repository.RepositoryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TranslationService {

    @Autowired
    PortalUserRepository portalUserRepository;

    @Autowired
    FilmRepository filmRepository;


    @Autowired
    RepositoryHelper repositoryHelper;

    public LikeReadDTO toRead(Like like) {

        LikeReadDTO dto = new LikeReadDTO();

        dto.setId(like.getId());
        dto.setType(like.getType());
        dto.setCommentId(like.getComment().getId());
        dto.setFilmId(like.getFilm().getId());

        return dto;
    }

    public ActorRoleReadDTO toRead(ActorRole actorRole) {

        ActorRoleReadDTO dto = new ActorRoleReadDTO();

        dto.setId(actorRole.getId());
        dto.setFilmId(actorRole.getFilm().getId());

        return dto;
    }

    public PortalUserReadDTO toRead(PortalUser portalUser) {

        PortalUserReadDTO dto = new PortalUserReadDTO();

        dto.setId(portalUser.getId());
        dto.setName(portalUser.getName());
        dto.setCountry(portalUser.getCountry());
        dto.setAge(portalUser.getAge());

        return dto;
    }

    public FilmReadDTO toRead(Film film) {

        FilmReadDTO dto = new FilmReadDTO();

        dto.setId(film.getId());
        dto.setFilmName(film.getFilmName());
        dto.setFilmYear(film.getFilmYear());
        dto.setFilmCountry(film.getFilmCountry());
        dto.setFilmScenario(film.getFilmScenario());
        dto.setFilmBudget(film.getFilmBudget());
        dto.setFilmDuration(film.getFilmDuration());

        return dto;
    }

    public CommentReadDTO toRead(Comment comment) {

        CommentReadDTO dto = new CommentReadDTO();

        dto.setId(comment.getId());
        dto.setDateAt(comment.getDateAt());
        dto.setStatus(comment.getStatus());
        dto.setPortalUserId(comment.getPortalUser().getId());
        dto.setFilmId(comment.getFilm().getId());

        return dto;
    }

    public ActorRole toEntity(ActorRoleCreateDTO create) {
        ActorRole actorRole = new ActorRole();
        return actorRole;
    }

    public Comment toEntity(CommentCreateDTO create) {

        Comment comment = new Comment();

        comment.setDateAt(create.getDateAt());
        comment.setStatus(CommentStatus.CHECKED);
        comment.setPortalUser(repositoryHelper.getReferenceIfExist(PortalUser.class, create.getPortalUserId()));
        comment.setFilm(repositoryHelper.getReferenceIfExist(Film.class, create.getFilmId()));

        return comment;
    }

    public PortalUser toEntity(PortalUserCreateDTO create) {

        PortalUser portalUser = new PortalUser();
        portalUser.setName(create.getName());
        portalUser.setCountry(create.getCountry());
        portalUser.setAge(create.getAge());

        return portalUser;
    }

    public void patchEntity(PortalUserPatchDTO patch, PortalUser portalUser) {

        if (patch.getName() != null) {
            portalUser.setName(patch.getName());
        }
        if (patch.getCountry() != null) {
            portalUser.setCountry(patch.getCountry());
        }
        if (patch.getAge() != null) {
            portalUser.setAge(patch.getAge());
        }
    }

    public void patchEntity(CommentPatchDTO patch, Comment comment) {

        if (patch.getDateAt() != null) {
            comment.setDateAt(patch.getDateAt());
        }

        if (patch.getStatus() != null) {
            comment.setStatus(patch.getStatus());
        }

    }

    public void patchEntity(LikePatchDTO patch, Like like) {

        if (patch.getType() != null) {
            like.setType(patch.getType());
        }

    }

    public CommentReadExtendedDTO toReadExtended(Comment comment) {

        CommentReadExtendedDTO dto = new CommentReadExtendedDTO();

        dto.setId(comment.getId());
        dto.setDateAt(comment.getDateAt());
        dto.setStatus(comment.getStatus());
        dto.setPortalUser(toRead(comment.getPortalUser()));
        dto.setFilm(toRead(comment.getFilm()));

        return dto;
    }


    public PortalUser createPortalUser(UUID id) {

        PortalUser portalUser = new PortalUser();

        portalUser.setId(id);
        portalUser.setName("Witcher");
        portalUser.setCountry("123");
        portalUser.setAge(25);

        return portalUserRepository.save(portalUser);
    }


    public Film createFilm(UUID id) {

        Film film = new Film();

        film.setId(id);
        film.setFilmName("Alone at home");
        film.setFilmYear(2008);
        film.setFilmCountry("USA");
        film.setFilmScenario("John Snow");
        film.setFilmBudget((long) 153856);
        film.setFilmDuration(89);

        return filmRepository.save(film);
    }
}