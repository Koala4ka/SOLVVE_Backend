package com.koala.backend.service;

import com.koala.backend.domain.Like;
import com.koala.backend.dto.like.LikeCreateDTO;
import com.koala.backend.dto.like.LikePatchDTO;
import com.koala.backend.dto.like.LikeReadDTO;
import com.koala.backend.exception.EntityNotFoundException;
import com.koala.backend.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private TranslationService translationService;

    public LikeReadDTO getLike(UUID id) {
        Like like = getLikeRequired(id);
        return toRead(like);
    }

    public void deleteLike(UUID id) {
        likeRepository.delete(getLikeRequired(id));
    }

    public LikeReadDTO patchLike(UUID id, LikePatchDTO patch) {

        Like like = getLikeRequired(id);

        translationService.patchEntity(patch, like);

        like = likeRepository.save(like);

        return translationService.toRead(like);
    }

    public LikeReadDTO createLike(LikeCreateDTO create) {
        Like like = new Like();
        like.setLike(create.getLike());
        like = likeRepository.save(like);
        return toRead(like);
    }

    private LikeReadDTO toRead(Like like) {
        LikeReadDTO dto = new LikeReadDTO();
        dto.setId(like.getId());
        dto.setLike(like.getLike());
        return dto;
    }

    private Like getLikeRequired(UUID id) {
        return likeRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(Like.class, id)
        );
    }
}