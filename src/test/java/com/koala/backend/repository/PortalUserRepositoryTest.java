package com.koala.backend.repository;

import com.koala.backend.domain.PortalUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = "delete from portal_user", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PortalUserRepositoryTest {

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Test
    public void testSave() {
        PortalUser p = new PortalUser();
        p = portalUserRepository.save(p);
        assertNotNull(p.getId());
        assertTrue(portalUserRepository.findById(p.getId()).isPresent());
    }
}
