package com.koala.backend.dto.film;

import lombok.Data;

@Data
public class FilmCreateDTO {

    private String filmName;
    private Integer filmYear;
    private String filmCountry;
    private String filmScenario;
    private Long filmBudget;
    private Integer filmDuration;
}
