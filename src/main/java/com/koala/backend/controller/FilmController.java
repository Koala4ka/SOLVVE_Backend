package com.koala.backend.controller;

import com.koala.backend.dto.film.FilmCreateDTO;
import com.koala.backend.dto.film.FilmPatchDTO;
import com.koala.backend.dto.film.FilmReadDTO;
import com.koala.backend.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/films")
public class FilmController {

    @Autowired
    private FilmService filmService;

    @GetMapping("/{id}")
    public FilmReadDTO getFilm(@PathVariable UUID id) {
        return filmService.getFilm(id);
    }

    @PostMapping
    public FilmReadDTO createFilm(@RequestBody FilmCreateDTO createDTO) {
        return filmService.createFilm(createDTO);
    }

    @PatchMapping("/{id}")
    public FilmReadDTO patchFilm(@PathVariable UUID id, @RequestBody FilmPatchDTO patch) {
        return filmService.patchFilm(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable UUID id) {
        filmService.deleteFilm(id);
    }

}
