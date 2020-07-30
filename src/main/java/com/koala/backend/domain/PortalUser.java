package com.koala.backend.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
public class PortalUser {

    @CreatedDate
    public Instant createdAt;

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String country;
    private Integer age;
}
