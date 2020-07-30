package com.koala.backend.dto.like;

import com.koala.backend.domain.LikedObjectType;
import lombok.Data;

import java.util.UUID;

@Data
public class LikePatchDTO {

    private UUID commentId;
    private UUID filmId;

    private Boolean like;

    private LikedObjectType type;
}
