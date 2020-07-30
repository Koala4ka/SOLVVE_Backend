package com.koala.backend.repository;

import com.koala.backend.domain.ActorRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActorRoleRepository extends CrudRepository<ActorRole, UUID> {

    List<ActorRole> findByFilmId(UUID filmId);
}
