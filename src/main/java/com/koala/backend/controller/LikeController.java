package com.koala.backend.controller;

import com.koala.backend.dto.like.LikeCreateDTO;
import com.koala.backend.dto.like.LikePatchDTO;
import com.koala.backend.dto.like.LikeReadDTO;
import com.koala.backend.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @GetMapping("/{id}")
    public LikeReadDTO getLike(@PathVariable UUID id) {
        return likeService.getLike(id);
    }

    @PostMapping
    public LikeReadDTO createLike(@RequestBody LikeCreateDTO createDTO) {
        return likeService.createLike(createDTO);
    }

    @PatchMapping("/{id}")
    public LikeReadDTO patchLike(@PathVariable UUID id, @RequestBody LikePatchDTO patch) {
        return likeService.patchLike(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteLike(@PathVariable UUID id) {
        likeService.deleteLike(id);
    }
}