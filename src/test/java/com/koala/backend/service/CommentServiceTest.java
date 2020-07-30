package com.koala.backend.service;

import com.koala.backend.domain.Comment;
import com.koala.backend.domain.CommentStatus;
import com.koala.backend.domain.Film;
import com.koala.backend.domain.PortalUser;
import com.koala.backend.dto.comment.*;
import com.koala.backend.exception.EntityNotFoundException;
import com.koala.backend.repository.CommentRepository;
import com.koala.backend.repository.FilmRepository;
import com.koala.backend.repository.PortalUserRepository;
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

import java.util.Set;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements =
        {"delete from comment",
                "delete from film",
                "delete from portal_user"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CommentServiceTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentExtendedService commentExtendedService;

    @Autowired
    private CreateService createService;


    @Test
    public void testGetComment() {

        Comment comment = createService.createComment();

        CommentReadDTO readDTO = commentService.getComment(comment.getId());

        Assertions.assertThat(readDTO.getId()).isEqualByComparingTo(comment.getId());
        Assertions.assertThat(readDTO.getDateAt()).isEqualTo(comment.getDateAt());
        Assertions.assertThat(readDTO.getStatus()).isEqualByComparingTo(comment.getStatus());
        Assertions.assertThat(readDTO.getPortalUserId()).isEqualByComparingTo(comment.getPortalUser().getId());
        Assertions.assertThat(readDTO.getFilmId()).isEqualByComparingTo(comment.getFilm().getId());
        Assertions.assertThat(readDTO).isNotEqualTo(comment);
    }

    @Test
    public void testGetCommentExtended() {

        PortalUser portalUser = createService.createPortalUser();
        Film film = createService.createFilm();

        Comment comment = createService.createComment(portalUser, film);

        CommentReadExtendedDTO read = commentExtendedService.getCommentExtended(comment.getId());
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(comment, "portalUser", "film");
        Assertions.assertThat(read.getPortalUser()).isEqualToIgnoringGivenFields(portalUser);
        //   Assertions.assertThat(read.getFilm()).isEqualToIgnoringGivenFields(film);
    }


    @Test(expected = EntityNotFoundException.class)
    public void testGetCommentWrongId() {

        commentService.getComment(UUID.randomUUID());
    }


    @Test
    public void testCreateComment() {

        PortalUser portalUser = createService.createPortalUser();
        Film film = createService.createFilm();

        CommentCreateDTO create = new CommentCreateDTO();
        create.setDateAt(null);
        create.setStatus(CommentStatus.CHECKED);
        create.setPortalUserId(portalUser.getId());
        create.setFilmId(film.getId());
        System.out.println(create.toString());

        CommentReadDTO read = commentService.createComment(create);
        Assert.assertNotNull(read.getId());

        Comment comment = commentRepository.findById(read.getId()).get();
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);
    }


    @Test
    public void testPatchComment() {

        Comment comment = createService.createComment();

        CommentPatchDTO patch = new CommentPatchDTO();

        patch.setDateAt(null);
        patch.setStatus(CommentStatus.CANCELLED);
        patch.setPortalUserId(comment.getPortalUser().getId());
        patch.setFilmId(comment.getFilm().getId());

        CommentReadDTO read = commentService.patchComment(comment.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        comment = commentRepository.findById(read.getId()).get();

    }

    @Test
    public void testPatchCommentEmptyPatch() {

        Comment comment = createService.createComment();

        CommentPatchDTO patch = new CommentPatchDTO();

        CommentReadDTO read = commentService.patchComment(comment.getId(), patch);

        Assert.assertNull(read.getDateAt());

        Comment commentAfterUpdate = commentRepository.findById(read.getId()).get();

        Assert.assertNull(commentAfterUpdate.getDateAt());

        //   Assertions.assertThat(comment).isEqualToComparingFieldByField(commentAfterUpdate);
    }

    @Test
    public void testUpdateComment() {

        Comment comment = createService.createComment();

        CommentPutDTO update = new CommentPutDTO();

        update.setDateAt(null);
        update.setStatus(CommentStatus.IN_CHECK);
        update.setPortalUserId(comment.getPortalUser().getId());
        update.setFilmId(comment.getFilm().getId());

        CommentReadDTO read = commentService.updateComment(comment.getId(), update);

        Assertions.assertThat(update).isEqualToComparingFieldByField(read);

    }

    @Test
    public void testDeleteComment() {
        Comment comment = createService.createComment();

        commentService.deleteComment(comment.getId());
        Assert.assertFalse(commentRepository.existsById(comment.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteCommentNotFound() {
        commentService.deleteComment(UUID.randomUUID());
    }

    @Test
    public void testGetCommentWithEmptyFilter() {

        PortalUser c1 = createService.createPortalUser();
        Film m1 = createService.createFilm();
        PortalUser c2 = createService.createPortalUser();
        Film m2 = createService.createFilm();

        Comment v1 = createService.createComment(c1, m1);
        Comment v2 = createService.createComment(c1, m2);
        Comment v3 = createService.createComment(c2, m2);

        CommentFilter filter = new CommentFilter();
        Assertions.assertThat(commentService.getComments(filter)).extracting("id")
                .containsExactlyInAnyOrder(v1.getId(), v2.getId(), v3.getId());

    }

    @Test
    public void testGetCommentByPortalUser() {

        PortalUser p1 = createService.createPortalUser();
        Film f1 = createService.createFilm();
        PortalUser p2 = createService.createPortalUser();
        Film f2 = createService.createFilm();

        Comment c1 = createService.createComment(p1, f1);
        Comment c2 = createService.createComment(p1, f2);
        createService.createComment(p2, f2);

        CommentFilter filter = new CommentFilter();
        filter.setPortalUserId(p1.getId());
        Assertions.assertThat(commentService.getComments(filter)).extracting("id")
                .containsExactlyInAnyOrder(c1.getId(), c2.getId());

    }

    @Test
    public void testGetCommentByFilm() {

        PortalUser p1 = createService.createPortalUser();
        Film f1 = createService.createFilm();
        PortalUser p2 = createService.createPortalUser();
        Film f2 = createService.createFilm();

        createService.createComment(p1, f1);
        Comment c2 = createService.createComment(p1, f2);
        Comment c3 = createService.createComment(p2, f2);

        CommentFilter filter = new CommentFilter();
        filter.setFilmId(f2.getId());
        Assertions.assertThat(commentService.getComments(filter)).extracting("id")
                .containsExactlyInAnyOrder(c2.getId(), c3.getId());

    }

    @Test
    public void testGetCommentByStatuses() {

        PortalUser c1 = createService.createPortalUser();
        Film m1 = createService.createFilm();
        PortalUser c2 = createService.createPortalUser();
        Film m2 = createService.createFilm();

        createService.createComment(c1, m1, CommentStatus.CHECKED, createService.createInstant(12));

        Comment v2 = createService.createComment(c1, m2, CommentStatus.CANCELLED, createService.createInstant(13));
        Comment v3 = createService.createComment(c2, m2, CommentStatus.IN_CHECK, createService.createInstant(9));

        CommentFilter filter = new CommentFilter();
        filter.setStatuses(Set.of(CommentStatus.CANCELLED, CommentStatus.IN_CHECK));

        Assertions.assertThat(commentService.getComments(filter)).extracting("id")
                .containsExactlyInAnyOrder(v2.getId(), v3.getId());
    }


    @Test
    public void testGetCommentByDateAtInterval() {

        PortalUser p1 = createService.createPortalUser();
        Film f1 = createService.createFilm();
        PortalUser p2 = createService.createPortalUser();
        Film f2 = createService.createFilm();

        Comment c1 = createService.createComment(p1, f1, CommentStatus.CHECKED, createService.createInstant(12));
        createService.createComment(p1, f2, CommentStatus.CANCELLED, createService.createInstant(13));
        Comment c3 = createService.createComment(p2, f2, CommentStatus.IN_CHECK, createService.createInstant(9));

        System.out.println(c1.getId().toString());
        System.out.println(c3.getId().toString());

        CommentFilter filter = new CommentFilter();

        filter.setDateAtFrom(createService.createInstant(9));
        filter.setDateAtTo(createService.createInstant(13));

        System.out.println(filter.toString());
        System.out.println("* * * *");
        System.out.println(commentService.getComments(filter).toString());

        Assertions.assertThat(commentService.getComments(filter)).extracting("id")
                .containsExactlyInAnyOrder(c1.getId(), c3.getId());
    }


    @Test
    public void testGetCommentByAllFilters() {

        PortalUser p1 = createService.createPortalUser();
        Film f1 = createService.createFilm();
        PortalUser p2 = createService.createPortalUser();
        Film f2 = createService.createFilm();

        Comment c1 = createService.createComment(p1, f1, CommentStatus.CHECKED, createService.createInstant(12));
        createService.createComment(p1, f2, CommentStatus.CANCELLED, createService.createInstant(13));
        createService.createComment(p2, f2, CommentStatus.IN_CHECK, createService.createInstant(9));

        System.out.println(c1.getId().toString());

        CommentFilter filter = new CommentFilter();

        filter.setPortalUserId(p1.getId());
        filter.setFilmId(f1.getId());

        filter.setDateAtFrom(createService.createInstant(8));
        filter.setDateAtTo(createService.createInstant(14));

        filter.setStatuses(Set.of(CommentStatus.CHECKED));

        Assertions.assertThat(commentService.getComments(filter)).extracting("id")
                .containsExactlyInAnyOrder(c1.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testCreateCommentWithWrongPortalUser() {

        Film f = createService.createFilm();

        CommentCreateDTO create = new CommentCreateDTO();
        create.setDateAt(createService.createInstant(4));
        create.setPortalUserId(UUID.randomUUID());
        create.setFilmId(f.getId());

        commentService.createComment(create);
    }

}
