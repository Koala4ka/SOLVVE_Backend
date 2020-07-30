package com.koala.backend.dto.comment;

import com.koala.backend.domain.CommentStatus;
import lombok.Data;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
public class CommentFilter {

    private UUID portalUserId;
    private UUID filmId;
    private Set<CommentStatus> statuses;
    private Instant dateAtFrom;
    private Instant dateAtTo;

}
