package com.koala.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koala.backend.domain.Comment;
import com.koala.backend.domain.CommentStatus;
import com.koala.backend.dto.comment.CommentCreateDTO;
import com.koala.backend.dto.comment.CommentFilter;
import com.koala.backend.dto.comment.CommentPatchDTO;
import com.koala.backend.dto.comment.CommentReadDTO;
import com.koala.backend.exception.EntityNotFoundException;
import com.koala.backend.service.CommentService;
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

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;


    @Test
    public void testGetComment() throws Exception {

        CommentReadDTO comment = getCommentReadDTO();

        Mockito.when(commentService.getComment(comment.getId())).thenReturn(comment);

        String resultJson = mvc.perform(get("/api/v1/comments/{id}", comment.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        CommentReadDTO actualComment = objectMapper.readValue(resultJson, CommentReadDTO.class);
        Assertions.assertThat(actualComment).isEqualToComparingFieldByField(comment);

        Mockito.verify(commentService).getComment(comment.getId());
    }

    @Test
    public void testGetCommentWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(Comment.class, wrongId);
        Mockito.when(commentService.getComment(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/comments/{id}", wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetCommentWrongTypeId() throws Exception {

        String id = "123";

        String resultJson = mvc.perform(get("/api/v1/comments/{id}", id))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("This UUID does not exist"));

    }

    @Test
    public void testCreateComment() throws Exception {

        CommentCreateDTO create = new CommentCreateDTO();
        create.setDateAt(null);

        CommentReadDTO read = getCommentReadDTO();

        Mockito.when(commentService.createComment(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/comments/")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CommentReadDTO actualComment = objectMapper.readValue(resultJson, CommentReadDTO.class);
        Assertions.assertThat(actualComment).isEqualToComparingFieldByField(read);

    }

    @Test
    public void testPatchComment() throws Exception {

        CommentPatchDTO patchDTO = new CommentPatchDTO();
        patchDTO.setDateAt(null);

        CommentReadDTO read = getCommentReadDTO();

        Mockito.when(commentService.patchComment(read.getId(), patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/comments/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CommentReadDTO actualComment = objectMapper.readValue(resultJson, CommentReadDTO.class);
        Assert.assertEquals(read, actualComment);
    }

    @Test
    public void testDeleteComment() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/comments/{id}", id.toString())).andExpect(status().isOk());

        Mockito.verify(commentService).deleteComment(id);
    }

    @Test
    public void testGetComments() throws Exception {

        CommentFilter commentFilter = new CommentFilter();

        commentFilter.setPortalUserId(UUID.randomUUID());
        commentFilter.setFilmId(UUID.randomUUID());
        commentFilter.setStatuses(Set.of(CommentStatus.CHECKED, CommentStatus.IN_CHECK));
        commentFilter.setDateAtFrom(Instant.now());
        commentFilter.setDateAtTo(Instant.now());

        CommentReadDTO read = new CommentReadDTO();

        read.setPortalUserId(commentFilter.getPortalUserId());
        read.setFilmId(commentFilter.getFilmId());
        read.setStatus(CommentStatus.CHECKED);
        read.setId(UUID.randomUUID());
        read.setDateAt(Instant.now());

        List<CommentReadDTO> expectedResult = List.of(read);
        Mockito.when(commentService.getComments(commentFilter)).thenReturn(expectedResult);

        String resultJson = mvc.perform(get("/api/v1/comments")
                .param("portalUserId", commentFilter.getPortalUserId().toString())
                .param("filmId", commentFilter.getFilmId().toString())
                .param("statuses", "CHECKED, IN_CHECK")
                .param("dateAtFrom", commentFilter.getDateAtFrom().toString())
                .param("dateAtTo", commentFilter.getDateAtTo().toString()))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<CommentReadDTO> actualResult = objectMapper.readValue(resultJson, new TypeReference<>() {
        });
        Assert.assertEquals(expectedResult, actualResult);
    }


    private CommentReadDTO getCommentReadDTO() {

        CommentReadDTO comment = new CommentReadDTO();
        comment.setId(UUID.randomUUID());
        comment.setDateAt(null);
        return comment;
    }

}
