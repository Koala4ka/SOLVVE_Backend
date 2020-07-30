package com.koala.backend.service;

import com.koala.backend.domain.PortalUser;
import com.koala.backend.dto.portaluser.PortalUserCreateDTO;
import com.koala.backend.dto.portaluser.PortalUserPatchDTO;
import com.koala.backend.dto.portaluser.PortalUserPutDTO;
import com.koala.backend.dto.portaluser.PortalUserReadDTO;
import com.koala.backend.exception.EntityNotFoundException;
import com.koala.backend.repository.PortalUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from portal_user", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PortalUserServiceTest {

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private PortalUserService portalUserService;

    private PortalUser createPortalUser() {

        PortalUser portalUser = new PortalUser();
        portalUser.setName("Joe");
        portalUser.setCountry("123");
        portalUser.setAge(25);

        return portalUserRepository.save(portalUser);
    }

    @Test
    public void testGetPortalUser() {

        PortalUser portalUser = createPortalUser();
        PortalUserReadDTO readDTO = portalUserService.getPortalUser(portalUser.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(portalUser);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetPortalUserWrongId() {

        portalUserService.getPortalUser(UUID.randomUUID());
    }

    @Test
    public void testCreatePortalUser() {

        PortalUserCreateDTO create = new PortalUserCreateDTO();
        create.setName("lila");
        create.setCountry("USA");
        create.setAge(20);
        PortalUserReadDTO read = portalUserService.createPortalUser(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);
        Assert.assertNotNull(read.getId());

        PortalUser portalUser = portalUserRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(portalUser);
    }

    @Test
    public void testPatchPortalUser() {

        PortalUser portalUser = createPortalUser();

        PortalUserPatchDTO patch = new PortalUserPatchDTO();
        patch.setName("Tom");
        patch.setCountry("Germany");
        patch.setAge(20);
        PortalUserReadDTO read = portalUserService.patchPortalUser(portalUser.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        portalUser = portalUserRepository.findById(read.getId()).get();

    }

    @Test
    public void testUpdatePortalUser() {

        PortalUser portalUser = createPortalUser();

        PortalUserPutDTO update = new PortalUserPutDTO();

        update.setName("Tom");
        update.setCountry("USA");
        update.setAge(20);

        PortalUserReadDTO read = portalUserService.updatePortalUser(portalUser.getId(), update);

        Assertions.assertThat(update).isEqualToComparingFieldByField(read);

    }

    @Test
    public void testPatchPortalUserEmptyPatch() {

        PortalUser portalUser = createPortalUser();

        PortalUserPatchDTO patch = new PortalUserPatchDTO();
        PortalUserReadDTO read = portalUserService.patchPortalUser(portalUser.getId(), patch);

        Assert.assertNotNull(read.getName());
        Assert.assertNotNull(read.getCountry());
        Assert.assertNotNull(read.getAge());

        PortalUser portalUserAfterUpdate = portalUserRepository.findById(read.getId()).get();

        Assert.assertNotNull(portalUserAfterUpdate.getName());
        Assert.assertNotNull(portalUserAfterUpdate.getCountry());
        Assert.assertNotNull(portalUserAfterUpdate.getAge());

        Assertions.assertThat(portalUser).isEqualToComparingFieldByField(portalUserAfterUpdate);
    }

    @Test
    public void testDeletePortalUser() {

        PortalUser portalUser = createPortalUser();

        portalUserService.deletePortalUser(portalUser.getId());
        Assert.assertFalse(portalUserRepository.existsById(portalUser.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeletePortalUserNotFound() {
        portalUserService.deletePortalUser(UUID.randomUUID());
    }
}
