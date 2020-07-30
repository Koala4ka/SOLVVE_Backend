package com.koala.backend.job;

import com.koala.backend.domain.Film;
import com.koala.backend.domain.PortalUser;
import com.koala.backend.repository.FilmRepository;
import com.koala.backend.service.FilmService;
import com.koala.backend.util.CreateService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Import(SchedulingConfigurationsTest.ScheduledTestConfig.class)
@ActiveProfiles("test")
public class SchedulingConfigurationsTest {

    @Autowired
    FilmRepository filmRepository;

    @SpyBean
    private FilmService filmService;

    @Autowired
    private CreateService createService;

    @Autowired
    private UpdateAverageMarkOfFilmsJob updateAverageMarkOfFilmsJob;

    @Test
    public void testSpringContextUpAndRunning() {
        log.info("@Scheduled configurations are OK");
    }


    @Test
    public void testUpdateAverageMarkOfFilms() {
        PortalUser p1 = createService.createPortalUser();
        Film f1 = createService.createFilm();

        createService.createComment(p1, f1, 3);
        createService.createComment(p1, f1, 5);
        updateAverageMarkOfFilmsJob.updateAverageMarkOfFilms();

        // f1 = filmRepository.findById(f1.getId()).get();
        //   Assert.assertEquals(4.0, f1.getAverageMark(),Double.MIN_NORMAL);
    }

    @Ignore
    @Test
    public void testFilmsUpdatedIndependently() {
        PortalUser p1 = createService.createPortalUser();
        Film f1 = createService.createFilm();
        Film f2 = createService.createFilm();

        createService.createComment(p1, f1, 3);
        createService.createComment(p1, f1, 5);

        UUID[] failedId = new UUID[1];
        Mockito.doAnswer(invocationOnMock -> {
            if (failedId[0] == null) {
                failedId[0] = invocationOnMock.getArgument(0);
                throw new RuntimeException();
            }
            return invocationOnMock.callRealMethod();
        }).when(filmService).updateAverageMarkOfFilm(Mockito.any());

        updateAverageMarkOfFilmsJob.updateAverageMarkOfFilms();

        for (Film f : filmRepository.findAll()) {
            if (f.getId().equals(failedId[0])) {
                Assert.assertNull(f.getAverageMark());
            } else {
                Assert.assertNotNull(f.getAverageMark());
            }
        }
    }

    @EnableScheduling
    static class ScheduledTestConfig {
    }
}
