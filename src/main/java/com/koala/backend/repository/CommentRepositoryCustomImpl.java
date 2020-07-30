package com.koala.backend.repository;

import com.koala.backend.domain.Comment;
import com.koala.backend.dto.comment.CommentFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Comment> findByFilter(CommentFilter filter) {

        StringBuilder sb = new StringBuilder();
        sb.append("select c from Comment c where 1=1");

        if (filter.getPortalUserId() != null) {
            sb.append(" and c.portalUser.id = :portalUserId");
        }
        if (filter.getFilmId() != null) {
            sb.append(" and c.film.id = :filmId");
        }
        if (filter.getStatuses() != null && !filter.getStatuses().isEmpty()) {
            sb.append(" and c.status in (:statuses)");
        }
        if (filter.getDateAtFrom() != null) {
            sb.append(" and c.dateAt >= (:dateAtFrom)");
        }
        if (filter.getDateAtTo() != null) {
            sb.append(" and c.dateAt < (:dateAtTo)");
        }

        TypedQuery<Comment> query = entityManager.createQuery(sb.toString(), Comment.class);

        if (filter.getPortalUserId() != null) {
            query.setParameter("portalUserId", filter.getPortalUserId());
        }
        if (filter.getFilmId() != null) {
            query.setParameter("filmId", filter.getFilmId());
        }
        if (filter.getStatuses() != null && !filter.getStatuses().isEmpty()) {
            query.setParameter("statuses", filter.getStatuses());
        }
        if (filter.getDateAtFrom() != null) {
            query.setParameter("dateAtFrom", filter.getDateAtFrom());
        }
        if (filter.getDateAtTo() != null) {
            query.setParameter("dateAtTo", filter.getDateAtTo());
        }

        return query.getResultList();
    }
}
