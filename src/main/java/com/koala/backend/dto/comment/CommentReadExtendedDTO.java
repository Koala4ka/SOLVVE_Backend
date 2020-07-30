package com.koala.backend.dto.comment;

import com.koala.backend.domain.CommentStatus;
import com.koala.backend.dto.film.FilmReadDTO;
import com.koala.backend.dto.portaluser.PortalUserReadDTO;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class CommentReadExtendedDTO {

    private UUID id;

    private PortalUserReadDTO portalUser;
    private FilmReadDTO film;

    private Instant dateAt;

    private CommentStatus status;
}
