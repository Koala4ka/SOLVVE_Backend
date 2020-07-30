package com.koala.backend.job;

import com.koala.backend.repository.FilmRepository;
import com.koala.backend.service.FilmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class UpdateAverageMarkOfFilmsJob {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private FilmService filmService;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Scheduled(cron = "${update.average.mark.of.films.job.cron}")
    public void updateAverageMarkOfFilms() {
        log.info("Job started");

        filmRepository.getIdsOfFilms().forEach(filmId -> {
            try {
                filmService.updateAverageMarkOfFilm(filmId);
            } catch (Exception e) {
                log.error("Failed to update average mark for film: {}", filmId, e);
            }
        });

        log.info("Job Finished");
    }
}