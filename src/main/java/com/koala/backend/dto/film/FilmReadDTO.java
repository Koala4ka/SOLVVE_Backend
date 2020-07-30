package com.koala.backend.dto.film;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class FilmReadDTO {

    private UUID id;

    private String filmName;
    private Integer filmYear;
    private String filmCountry;
    private String filmScenario;
    private Long filmBudget;
    private Integer filmDuration;

    private Instant createdAt;
}
