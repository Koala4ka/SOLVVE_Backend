package com.koala.backend.repository;

import com.koala.backend.domain.Film;
import com.koala.backend.util.CreateService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = "delete from film", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class FilmRepositoryTest {

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private CreateService createService;

    @Test
    public void testSave() {
        Film f = new Film();
        f = filmRepository.save(f);
        assertNotNull(f.getId());
        assertTrue(filmRepository.findById(f.getId()).isPresent());
    }

    @Test
    public void testCreatedAtIsSet() {

        Film f = new Film();
        f = filmRepository.save(f);

        Instant createdAtBeforeReload = f.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        f = filmRepository.save(f);

        Instant createdAtAfterReload = f.getCreatedAt();
        Assert.assertNotNull(createdAtAfterReload);
        Assert.assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testGetIdsOfFilms() {
        Set<UUID> expectedIdsOfFilms = new HashSet<>();
        expectedIdsOfFilms.add(createService.createFilm().getId());
        expectedIdsOfFilms.add(createService.createFilm().getId());

        transactionTemplate.executeWithoutResult(status -> {
            Assert.assertEquals(expectedIdsOfFilms, filmRepository.getIdsOfFilms().collect(Collectors.toSet()));
        });
    }
}

