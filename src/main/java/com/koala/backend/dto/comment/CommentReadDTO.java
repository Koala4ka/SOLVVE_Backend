package com.koala.backend.dto.comment;

import com.koala.backend.domain.CommentStatus;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class CommentReadDTO {

    private UUID id;

    private UUID portalUserId;
    private UUID filmId;

    private Instant dateAt;

    private CommentStatus status;

    private Instant updatedAt;

    private Instant createdAt;

}
