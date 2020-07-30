package com.koala.backend.repository;

import com.koala.backend.domain.PortalUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PortalUserRepository extends CrudRepository<PortalUser, UUID> {
}
