package com.koala.backend.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
public class Like {

    @Id
    @GeneratedValue
    private UUID id;

    private Boolean like;

    private UUID likedObjectId;

    @Enumerated(EnumType.STRING)
    private LikedObjectType type;

    @ManyToOne
    private Comment comment;

    @ManyToOne
    private Film film;
}