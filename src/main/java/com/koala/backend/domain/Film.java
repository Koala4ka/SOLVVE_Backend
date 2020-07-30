package com.koala.backend.domain;

import lombok.Data;
import org.dom4j.tree.AbstractEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Film extends AbstractEntity {

    @CreatedDate
    public Instant createdAt;

    @Id
    @GeneratedValue
    private UUID id;
    private String filmName;
    private Integer filmYear;
    private String filmCountry;
    private String filmScenario;
    private Long filmBudget;
    private Integer filmDuration;
    private Double averageMark;

    @OneToMany(mappedBy = "film")
    private List<ActorRole> actorRole;


}

