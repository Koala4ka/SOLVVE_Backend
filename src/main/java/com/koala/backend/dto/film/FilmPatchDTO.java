package com.koala.backend.dto.film;

import lombok.Data;

@Data
public class FilmPatchDTO {

    private String filmName;
    private Integer filmYear;
    private String filmCountry;
    private String filmScenario;
    private Long filmBudget;
    private Integer filmDuration;
}
