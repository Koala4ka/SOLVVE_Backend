package com.koala.backend.dto.like;

import com.koala.backend.domain.LikedObjectType;
import lombok.Data;

import java.util.UUID;

@Data
public class LikeReadDTO {

    private UUID id;
    private UUID commentId;
    private UUID filmId;
    private UUID likedObjectId;

    private Boolean like;

    private LikedObjectType type;
}




