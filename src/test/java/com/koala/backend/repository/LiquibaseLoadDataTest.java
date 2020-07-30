package com.koala.backend.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.xml")
@Sql(statements =
        {"delete from actor_role",
                "delete from film",
                "delete from portal_user"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class LiquibaseLoadDataTest {

    @Autowired
    ActorRoleRepository actorRoleRepository;

    @Autowired
    FilmRepository filmRepository;

    @Autowired
    PortalUserRepository portalUserRepository;

    @Test
    public void testDataLoaded() {

        Assert.assertTrue(portalUserRepository.count() > 0);
        Assert.assertTrue(filmRepository.count() > 0);
        Assert.assertTrue(actorRoleRepository.count() > 0);
    }

}
