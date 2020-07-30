package com.koala.backend.repository;

import com.koala.backend.domain.PortalUser;
import com.koala.backend.exception.EntityNotFoundException;
import com.koala.backend.util.CreateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.UUID;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = "delete from portal_user", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RepositoryHelperTest {

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private RepositoryHelper repositoryHelper;

    @Autowired
    private CreateService createService;

    @Test
    public void testGetPortalUserThroughRepositoryHelper() {

        PortalUser portalUser = createService.createPortalUser();

        PortalUser portalUser1 = repositoryHelper.getReferenceIfExist(PortalUser.class, portalUser.getId());

        // Assertions.assertThat(portalUser1).isEqualToComparingFieldByField(portalUser);
        Assert.notNull(portalUser1, "not null");

    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetPortalUserThroughRepositoryHelperByWrongId() {

        PortalUser portalUser1 = repositoryHelper.getReferenceIfExist(PortalUser.class, UUID.randomUUID());

    }


}
