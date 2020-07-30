package com.koala.backend.controller;

import com.koala.backend.dto.comment.CommentReadExtendedDTO;
import com.koala.backend.service.CommentExtendedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentExtendedController {

    @Autowired
    private CommentExtendedService commentExtendedService;

    @GetMapping("/{id}/extended")
    public CommentReadExtendedDTO getCommentExtended(@PathVariable UUID id) {
        return commentExtendedService.getCommentExtended(id);
    }

}
