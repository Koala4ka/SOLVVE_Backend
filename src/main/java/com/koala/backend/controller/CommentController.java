package com.koala.backend.controller;

import com.koala.backend.dto.comment.*;
import com.koala.backend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/{id}")
    public CommentReadDTO getComment(@PathVariable UUID id) {
        return commentService.getComment(id);
    }

    @GetMapping
    public List<CommentReadDTO> getComments(CommentFilter filter) {
        return commentService.getComments(filter);
    }

    @PostMapping
    public CommentReadDTO createComment(@RequestBody CommentCreateDTO create) {
        return commentService.createComment(create);
    }

    @PatchMapping("/{id}")
    public CommentReadDTO patchComment(@PathVariable UUID id, @RequestBody CommentPatchDTO patch) {
        return commentService.patchComment(id, patch);
    }

    @PutMapping
    public CommentReadDTO putComment(@PathVariable UUID id, @RequestBody CommentPutDTO put) {
        return commentService.updateComment(id, put);
    }


    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable UUID id) {
        commentService.deleteComment(id);
    }

}
