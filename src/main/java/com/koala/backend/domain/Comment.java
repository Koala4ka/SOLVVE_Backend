package com.koala.backend.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @Id
    @GeneratedValue
    private UUID id;
    private Instant dateAt;
    private Double portalUserMark;

    @Enumerated(EnumType.STRING)
    private CommentStatus status;

    @ManyToOne
    private PortalUser portalUser;

    @ManyToOne
    private Film film;

    @LastModifiedDate
    private Instant updatedAt;

    @CreatedDate
    private Instant createdAt;

}
