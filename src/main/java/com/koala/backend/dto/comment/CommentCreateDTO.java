package com.koala.backend.dto.comment;

import com.koala.backend.domain.CommentStatus;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class CommentCreateDTO {

    private UUID portalUserId;
    private UUID filmId;

    private Instant dateAt;

    private CommentStatus status;
}
