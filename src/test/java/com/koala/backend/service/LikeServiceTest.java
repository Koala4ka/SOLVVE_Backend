package com.koala.backend.service;

import com.koala.backend.domain.Like;
import com.koala.backend.domain.LikedObjectType;
import com.koala.backend.dto.like.LikeReadDTO;
import com.koala.backend.exception.EntityNotFoundException;
import com.koala.backend.repository.LikeRepository;
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
@Sql(statements = "delete from like", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class LikeServiceTest {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private LikeService likeService;

    @Test
    public void testGetLike() {

        Like like = createLike();
        LikeReadDTO readDTO = likeService.getLike(like.getId());
        //Assertions.assertThat(readDTO).isEqualToComparingFieldByField(like);
    }

    private Like createLike() {

        Like like = new Like();
        like.setType(LikedObjectType.FILM);
        //  like.setComment(createLike());

        return likeRepository.save(like);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetLikeWrongId() {

        likeService.getLike(UUID.randomUUID());
    }

    @Test
    public void testDeleteLike() {

        Like like = createLike();

        likeService.deleteLike(like.getId());
        Assert.assertFalse(likeRepository.existsById(like.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteLikeNotFound() {
        likeService.deleteLike(UUID.randomUUID());
    }
}

