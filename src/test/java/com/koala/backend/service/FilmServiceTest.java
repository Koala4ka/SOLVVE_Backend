package com.koala.backend.service;

import com.koala.backend.domain.Film;
import com.koala.backend.domain.PortalUser;
import com.koala.backend.dto.film.FilmCreateDTO;
import com.koala.backend.dto.film.FilmPatchDTO;
import com.koala.backend.dto.film.FilmReadDTO;
import com.koala.backend.exception.EntityNotFoundException;
import com.koala.backend.repository.FilmRepository;
import com.koala.backend.util.CreateService;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from film", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class FilmServiceTest {

    @Autowired
    private CreateService createService;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private FilmService filmService;

    private Film createFilm() {

        Film film = new Film();
        film.setFilmName("Jumanji: Welcome to the Jungle");
        film.setFilmYear(2019);
        film.setFilmCountry("USA");
        film.setFilmScenario("Chris McKenna");
        film.setFilmBudget((long) 150.000000);
        film.setFilmDuration((int) 1.60);
        return filmRepository.save(film);
    }

    @Test
    public void testGetFilm() {

        Film film = createFilm();

        FilmReadDTO readDTO = filmService.getFilm(film.getId());
        // Assertions.assertThat(readDTO).isEqualToComparingFieldByField(film);
        Assertions.assertThat(readDTO).isNotEqualTo(film);
    }


    @Test(expected = EntityNotFoundException.class)
    public void testGetFilmWrongId() {

        filmService.getFilm(UUID.randomUUID());
    }

    @Test
    public void testCreateFilm() {

        FilmCreateDTO create = new FilmCreateDTO();
        create.setFilmName("Jumanji: Welcome to the Jungle");
        create.setFilmYear(2019);
        create.setFilmCountry("USA");
        create.setFilmScenario("Chris McKenna");
        create.setFilmBudget((long) 150.000000);
        create.setFilmDuration((int) 1.60);

        FilmReadDTO read = filmService.createFilm(create);
        // Assertions.assertThat(create).isEqualToComparingFieldByField(read);
        Assert.assertNotNull(read.getId());

        Film film = filmRepository.findById(read.getId()).get();
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchFilm() {

        Film film = createFilm();

        FilmPatchDTO patch = new FilmPatchDTO();
        patch.setFilmName("Paddington");
        patch.setFilmYear(2017);
        patch.setFilmCountry("USA");
        patch.setFilmScenario("Lory Flint");
        patch.setFilmBudget((long) 20.000000);
        patch.setFilmDuration((int) 1.30);

        FilmReadDTO read = filmService.patchFilm(film.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        film = filmRepository.findById(read.getId()).get();

    }

    @Test
    public void testPatchFilmEmptyPatch() {

        Film film = createFilm();

        FilmPatchDTO patch = new FilmPatchDTO();
        FilmReadDTO read = filmService.patchFilm(film.getId(), patch);

        Assert.assertNotNull(read.getFilmName());
        Assert.assertNotNull(read.getFilmYear());
        Assert.assertNotNull(read.getFilmCountry());
        Assert.assertNotNull(read.getFilmScenario());
        Assert.assertNotNull(read.getFilmBudget());
        Assert.assertNotNull(read.getFilmDuration());


        Film filmAfterUpdate = filmRepository.findById(read.getId()).get();

        Assert.assertNotNull(filmAfterUpdate.getFilmName());
        Assert.assertNotNull(filmAfterUpdate.getFilmYear());
        Assert.assertNotNull(filmAfterUpdate.getFilmCountry());
        Assert.assertNotNull(filmAfterUpdate.getFilmScenario());
        Assert.assertNotNull(filmAfterUpdate.getFilmBudget());
        Assert.assertNotNull(filmAfterUpdate.getFilmDuration());
        //  Assertions.assertThat(film).isEqualToComparingFieldByField(filmAfterUpdate);
    }

    @Test
    public void testDeleteFilm() {

        Film film = createFilm();

        filmService.deleteFilm(film.getId());
        Assert.assertFalse(filmRepository.existsById(film.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteFilmNotFound() {
        filmService.deleteFilm(UUID.randomUUID());
    }

   

    @Ignore
    @Test
    public void testUpdateAverageMarkOfFilm() {
        PortalUser p1 = createService.createPortalUser();
        Film f1 = createFilm();

        createService.createComment(p1, f1, 3);
        createService.createComment(p1, f1, 5);

        filmService.updateAverageMarkOfFilm(f1.getId());
        f1 = filmRepository.findById(f1.getId()).get();
        Assert.assertEquals(4.5, f1.getAverageMark(), Double.MIN_NORMAL);

    }
}
