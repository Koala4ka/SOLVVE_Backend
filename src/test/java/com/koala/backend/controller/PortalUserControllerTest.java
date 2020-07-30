package com.koala.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koala.backend.domain.PortalUser;
import com.koala.backend.dto.portaluser.PortalUserCreateDTO;
import com.koala.backend.dto.portaluser.PortalUserPatchDTO;
import com.koala.backend.dto.portaluser.PortalUserReadDTO;
import com.koala.backend.exception.EntityNotFoundException;
import com.koala.backend.service.PortalUserService;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PortalUserController.class)
public class PortalUserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PortalUserService portalUserService;


    @Test
    public void testGetPortalUser() throws Exception {
        PortalUserReadDTO portalUser = getPortalUserReadDTO();

        Mockito.when(portalUserService.getPortalUser(portalUser.getId())).thenReturn(portalUser);

        String resultJson = mvc.perform(get("/api/v1/portalUsers/{id}", portalUser.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        PortalUserReadDTO actualPortalUser = objectMapper.readValue(resultJson, PortalUserReadDTO.class);
        Assertions.assertThat(actualPortalUser).isEqualToComparingFieldByField(portalUser);

        Mockito.verify(portalUserService).getPortalUser(portalUser.getId());
    }

    @Test
    public void testCreatePortalUser() throws Exception {

        PortalUserCreateDTO create = new PortalUserCreateDTO();
        create.setName("Joe");
        create.setCountry("123");
        create.setAge(25);

        PortalUserReadDTO read = getPortalUserReadDTO();

        Mockito.when(portalUserService.createPortalUser(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/portalUsers/")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PortalUserReadDTO actualPortalUser = objectMapper.readValue(resultJson, PortalUserReadDTO.class);
        Assertions.assertThat(actualPortalUser).isEqualToComparingFieldByField(read);

    }

    @Test
    public void testGetPortalUserWrongId() throws Exception {

        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(PortalUser.class, wrongId);
        Mockito.when(portalUserService.getPortalUser(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/portalUsers/{id}", wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetPortalUserWrongTypeId() throws Exception {

        String id = "123";

        String resultJson = mvc.perform(get("/api/v1/portalUsers/{id}", id))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("This UUID does not exist"));

    }

    @Test
    public void testPatchPortalUser() throws Exception {

        PortalUserPatchDTO patchDTO = new PortalUserPatchDTO();
        patchDTO.setName("Joe");
        patchDTO.setCountry("123");

        PortalUserReadDTO read = getPortalUserReadDTO();

        Mockito.when(portalUserService.patchPortalUser(read.getId(), patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/portalUsers/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PortalUserReadDTO actualPortalUser = objectMapper.readValue(resultJson, PortalUserReadDTO.class);
        Assert.assertEquals(read, actualPortalUser);
    }

    @Test
    public void testDeletePortalUser() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/portalUsers/{id}", id.toString())).andExpect(status().isOk());

        Mockito.verify(portalUserService).deletePortalUser(id);
    }

    private PortalUserReadDTO getPortalUserReadDTO() {

        PortalUserReadDTO portalUser = new PortalUserReadDTO();
        portalUser.setId(UUID.randomUUID());
        portalUser.setName("Joe");
        portalUser.setCountry("USA");
        portalUser.setAge(25);
        return portalUser;
    }

}
