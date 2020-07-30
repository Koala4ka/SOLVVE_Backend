package com.koala.backend.service;

import com.koala.backend.domain.Film;
import com.koala.backend.dto.film.FilmCreateDTO;
import com.koala.backend.dto.film.FilmPatchDTO;
import com.koala.backend.dto.film.FilmReadDTO;
import com.koala.backend.exception.EntityNotFoundException;
import com.koala.backend.repository.CommentRepository;
import com.koala.backend.repository.FilmRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class FilmService {


    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private CommentRepository commentRepository;

    public FilmReadDTO getFilm(UUID id) {
        Film film = getFilmRequired(id);
        return toRead(film);
    }

    public FilmReadDTO toRead(Film film) {
        FilmReadDTO dto = new FilmReadDTO();
        dto.setId(film.getId());
        dto.setFilmName(film.getFilmName());
        dto.setFilmYear(film.getFilmYear());
        dto.setFilmCountry(film.getFilmCountry());
        dto.setFilmScenario(film.getFilmScenario());
        dto.setFilmBudget(film.getFilmBudget());
        dto.setFilmDuration(film.getFilmDuration());
        return dto;
    }

    public FilmReadDTO createFilm(FilmCreateDTO create) {
        Film film = new Film();
        film.setFilmName(create.getFilmName());
        film.setFilmYear(create.getFilmYear());
        film.setFilmCountry(create.getFilmCountry());
        film.setFilmScenario(create.getFilmScenario());
        film.setFilmBudget(create.getFilmBudget());
        film.setFilmDuration(create.getFilmDuration());

        film = filmRepository.save(film);
        return toRead(film);
    }

    public FilmReadDTO patchFilm(UUID id, FilmPatchDTO patch) {
        Film film = getFilmRequired(id);

        if (patch.getFilmName() != null) {
            film.setFilmName(patch.getFilmName());
        }

        if (patch.getFilmYear() != null) {
            film.setFilmYear(patch.getFilmYear());
        }
        if (patch.getFilmCountry() != null) {
            film.setFilmCountry(patch.getFilmCountry());
        }

        if (patch.getFilmScenario() != null) {
            film.setFilmScenario(patch.getFilmScenario());
        }

        if (patch.getFilmBudget() != null) {
            film.setFilmBudget(patch.getFilmBudget());
        }
        if (patch.getFilmDuration() != null) {
            film.setFilmDuration(patch.getFilmDuration());
        }

        film = filmRepository.save(film);
        return toRead(film);
    }

    public void deleteFilm(UUID id) {
        filmRepository.delete(getFilmRequired(id));
    }

    public Film getFilmRequired(UUID id) {
        return filmRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(Film.class, id)
        );
    }

    public void updateAverageMarkOfFilm(UUID filmId) {
        Double averageMark = commentRepository.calcAverageMarkOfFilm(filmId);
        Film film = filmRepository.findById(filmId).orElseThrow(
                () -> new EntityNotFoundException(Film.class, filmId));

        log.info("Setting new average mark of film: {}.Old value: {}, new value: {}", filmId,
                film.getAverageMark(), averageMark);
        film.setAverageMark(averageMark);
        filmRepository.save(film);
    }

}
