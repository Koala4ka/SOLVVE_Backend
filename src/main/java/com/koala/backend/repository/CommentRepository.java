package com.koala.backend.repository;

import com.koala.backend.domain.Comment;
import com.koala.backend.domain.CommentStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends CrudRepository<Comment, UUID>, CommentRepositoryCustom {

    List<Comment> findByFilmIdAndStatusOrderByDateAtAsc(UUID filmId, CommentStatus commentStatus);

    @Query("select c from Comment c where c.film.id = :filmId and c.status = :commentStatus"
            + " and c.dateAt >= :dateFrom and c.dateAt < :dateTo order by c.dateAt asc")
    List<Comment> findCommentsForFilmInGivenInternal(
            UUID filmId, CommentStatus commentStatus, Instant dateFrom, Instant dateTo);

    @Query("select avg(c.portalUserMark) from Comment c where c.film.id = :filmId")
    Double calcAverageMarkOfFilm(UUID filmId);

}
