package com.koala.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koala.backend.domain.Film;
import com.koala.backend.dto.film.FilmCreateDTO;
import com.koala.backend.dto.film.FilmPatchDTO;
import com.koala.backend.dto.film.FilmReadDTO;
import com.koala.backend.exception.EntityNotFoundException;
import com.koala.backend.service.FilmService;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(FilmController.class)
public class FilmControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FilmService filmService;


    @Test
    public void testGetFilm() throws Exception {

        FilmReadDTO film = getFilmReadDTO();

        Mockito.when(filmService.getFilm(film.getId())).thenReturn(film);

        String resultJson = mvc.perform(get("/api/v1/films/{id}", film.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        FilmReadDTO actualFilm = objectMapper.readValue(resultJson, FilmReadDTO.class);
        Assertions.assertThat(actualFilm).isEqualToComparingFieldByField(film);

        Mockito.verify(filmService).getFilm(film.getId());
    }

    @Test
    public void testCreateFilm() throws Exception {

        FilmCreateDTO create = new FilmCreateDTO();
        create.setFilmName("Jumanji: Welcome to the Jungle");
        create.setFilmYear(2019);
        create.setFilmCountry("USA");
        create.setFilmScenario("Chris McKenna");
        create.setFilmBudget((long) 150.000000);
        create.setFilmDuration((int) 1.60);

        FilmReadDTO read = getFilmReadDTO();

        Mockito.when(filmService.createFilm(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/films/")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        FilmReadDTO actualFilm = objectMapper.readValue(resultJson, FilmReadDTO.class);
        Assertions.assertThat(actualFilm).isEqualToComparingFieldByField(read);

    }

    @Test
    public void testGetFilmWrongId() throws Exception {

        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(Film.class, wrongId);
        Mockito.when(filmService.getFilm(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/films/{id}", wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetFilmWrongTypeId() throws Exception {

        String id = "123";

        String resultJson = mvc.perform(get("/api/v1/films/{id}", id))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("This UUID does not exist"));

    }

    @Test
    public void testPatchFilm() throws Exception {

        FilmPatchDTO patchDTO = new FilmPatchDTO();
        patchDTO.setFilmName("Jumanji: Welcome to the Jungle");
        patchDTO.setFilmYear(2019);
        patchDTO.setFilmCountry("USA");
        patchDTO.setFilmScenario("Chris McKenna");
        patchDTO.setFilmBudget((long) 150.000000);
        patchDTO.setFilmDuration((int) 1.60);


        FilmReadDTO read = getFilmReadDTO();

        Mockito.when(filmService.patchFilm(read.getId(), patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/films/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        FilmReadDTO actualFilm = objectMapper.readValue(resultJson, FilmReadDTO.class);
        Assert.assertEquals(read, actualFilm);
    }

    @Test
    public void testDeleteFilm() throws Exception {

        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/films/{id}", id.toString())).andExpect(status().isOk());

        Mockito.verify(filmService).deleteFilm(id);
    }

    private FilmReadDTO getFilmReadDTO() {

        FilmReadDTO film = new FilmReadDTO();
        film.setId(UUID.randomUUID());
        film.setFilmName("Jumanji: Welcome to the Jungle");
        film.setFilmYear(2019);
        film.setFilmCountry("USA");
        film.setFilmScenario("Chris McKenna");
        film.setFilmBudget((long) 150.000000);
        film.setFilmDuration((int) 1.60);
        return film;
    }
}