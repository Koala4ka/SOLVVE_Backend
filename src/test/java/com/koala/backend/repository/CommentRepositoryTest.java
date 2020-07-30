package com.koala.backend.repository;

import com.koala.backend.domain.Comment;
import com.koala.backend.domain.CommentStatus;
import com.koala.backend.domain.Film;
import com.koala.backend.domain.PortalUser;
import com.koala.backend.dto.comment.CommentPutDTO;
import com.koala.backend.dto.comment.CommentReadDTO;
import com.koala.backend.service.CommentService;
import com.koala.backend.util.CreateService;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements =
        {"delete from comment",
                "delete from film",
                "delete from portal_user"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CreateService createService;

    @Autowired
    private CommentService commentService;

    @Test
    public void testSave() {

        Comment c = new Comment();
        c = commentRepository.save(c);
        assertNotNull(c.getId());
        assertTrue(commentRepository.findById(c.getId()).isPresent());
    }

    @Test
    public void testGetFilmComments() {

        PortalUser c = createService.createPortalUser();
        Film f1 = createService.createFilm();
        Film f2 = createService.createFilm();
        Comment v1 = createService.createComment(c, f1, CommentStatus.CHECKED);
        createService.createComment(c, f1, CommentStatus.CANCELLED);
        Comment v2 = createService.createComment(c, f1, CommentStatus.CHECKED);
        createService.createComment(c, f2, CommentStatus.CHECKED);

        List<Comment> res = commentRepository.findByFilmIdAndStatusOrderByDateAtAsc(f1.getId(), CommentStatus.CHECKED);
        Assertions.assertThat(res).extracting(Comment::getId).isEqualTo(Arrays.asList(v1.getId(), v2.getId()));
    }


    @Test
    public void testFindCommentsForFilmInGivenInternal() {

        PortalUser c = createService.createPortalUser();
        Film f1 = createService.createFilm();
        Film f2 = createService.createFilm();
        ZoneOffset utc = ZoneOffset.UTC;

        Comment c1 = createService.createComment(c, f1, CommentStatus.CHECKED,
                LocalDateTime.of(2019, 12, 4, 15, 0, 0).toInstant(utc));
        createService.createComment(c, f1, CommentStatus.CANCELLED,
                LocalDateTime.of(2019, 12, 4, 15, 5, 0).toInstant(utc));

        Comment c2 = createService.createComment(c, f1, CommentStatus.CHECKED,
                LocalDateTime.of(2019, 12, 4, 15, 30, 0).toInstant(utc));
        createService.createComment(c, f1, CommentStatus.CHECKED,
                LocalDateTime.of(2019, 12, 4, 17, 30, 0).toInstant(utc));
        createService.createComment(c, f1, CommentStatus.CHECKED,
                LocalDateTime.of(2019, 12, 4, 10, 5, 0).toInstant(utc));
        createService.createComment(c, f2, CommentStatus.CHECKED,
                LocalDateTime.of(2019, 12, 4, 15, 0, 0).toInstant(utc));
        createService.createComment(c, f2, CommentStatus.CHECKED,
                LocalDateTime.of(2020, 1, 27, 18, 30, 0).toInstant(utc));


        List<Comment> res = commentRepository.findCommentsForFilmInGivenInternal(f1.getId(), CommentStatus.CHECKED,
                LocalDateTime.of(2019, 12, 4, 15, 0, 0).toInstant(utc),
                LocalDateTime.of(2019, 12, 4, 17, 30, 0).toInstant(utc));
        Assertions.assertThat(res).extracting(Comment::getId).isEqualTo(Arrays.asList(c1.getId(), c2.getId()));
    }

    @Test
    public void testCreatedAtIsSet() {
        PortalUser p = createService.createPortalUser();
        Film f = createService.createFilm();

        Comment c = new Comment();
        c.setPortalUser(p);
        c.setFilm(f);
        c.setDateAt(LocalDateTime.of(2019, 12, 4, 15, 0, 0)
                .toInstant(ZoneOffset.UTC));
        c.setDateAt(c.getDateAt().plus(1, ChronoUnit.HOURS));
        c.setStatus(CommentStatus.CHECKED);
        c = commentRepository.save(c);

        Instant createdAtBeforeReload = c.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        c = commentRepository.save(c);

        Instant createdAtAfterReload = c.getCreatedAt();
        Assert.assertNotNull(createdAtAfterReload);
        Assert.assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsSet() {

        PortalUser p = createService.createPortalUser();
        Film f = createService.createFilm();

        Comment c = new Comment();
        c.setPortalUser(p);
        c.setFilm(f);
        c.setDateAt(LocalDateTime.of(2020, 01, 10, 22, 15, 0).toInstant(ZoneOffset.UTC));
        c.setDateAt(c.getDateAt().plus(1, ChronoUnit.HOURS));
        c.setStatus(CommentStatus.CHECKED);
        c = commentRepository.save(c);

        CommentPutDTO update = new CommentPutDTO();
        update.setPortalUserId(p.getId());
        update.setFilmId(f.getId());
        update.setDateAt(c.getDateAt());
        update.setStatus(CommentStatus.CANCELLED);

        Instant updateAtBeforeUpdate = c.getUpdatedAt();
        Assert.assertNotNull(updateAtBeforeUpdate);
        System.out.println("c  ***************");
        System.out.print("c.getCreatedAt()    ");
        System.out.println(c.getCreatedAt());
        System.out.print("c.getUpdatedAt()    ");
        System.out.println(c.getUpdatedAt());

        Instant createdAtBeforeUpdated = c.getCreatedAt();
        Instant updatedAtBeforeUpdated = c.getUpdatedAt();
        Assert.assertNotNull(createdAtBeforeUpdated);
        Assert.assertNotNull(updatedAtBeforeUpdated);
        Assert.assertEquals(createdAtBeforeUpdated, updatedAtBeforeUpdated);

        CommentReadDTO readDTO = commentService.updateComment(c.getId(), update);

        c = commentRepository.findById(c.getId()).get();

        System.out.println("c from repository  ********");
        System.out.print("c.getCreatedAt()    ");
        System.out.println(c.getCreatedAt());
        System.out.print("c.getUpdatedAt()    ");
        System.out.println(c.getUpdatedAt());

        Instant updatedAtAfterUpdated = c.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterUpdated);
        Assert.assertNotEquals(updatedAtBeforeUpdated, updatedAtAfterUpdated);
    }

    @Test
    public void testCalcAverageMark() {
        PortalUser p1 = createService.createPortalUser();
        Film f1 = createService.createFilm();
        Film f2 = createService.createFilm();

        createService.createComment(p1, f1, 5);
        createService.createComment(p1, f2, 5);
        //   createService.createComment(p1, );
        createService.createComment(p1, f2, 2);

        Assert.assertEquals(4.5, commentRepository.calcAverageMarkOfFilm(f1.getId()), Double.MIN_NORMAL);

    }
}
