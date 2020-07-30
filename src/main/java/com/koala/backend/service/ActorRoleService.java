package com.koala.backend.service;

import com.koala.backend.domain.ActorRole;
import com.koala.backend.domain.Film;
import com.koala.backend.dto.actorrole.ActorRoleCreateDTO;
import com.koala.backend.dto.actorrole.ActorRoleReadDTO;
import com.koala.backend.repository.ActorRoleRepository;
import com.koala.backend.repository.RepositoryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class ActorRoleService {

    @Autowired
    ActorRoleRepository actorRoleRepository;

    @Autowired
    TranslationService translationService;

    @Autowired
    RepositoryHelper repositoryHelper;

    public List<ActorRoleReadDTO> getFilmRoles(UUID filmId) {

        List<ActorRole> roles = actorRoleRepository.findByFilmId(filmId);
        return roles.stream().map(translationService::toRead).collect(Collectors.toList());
    }

    public ActorRoleReadDTO createActorRole(UUID filmId, ActorRoleCreateDTO create) {

        ActorRole actorRole = translationService.toEntity(create);
        actorRole.setFilm(repositoryHelper.getReferenceIfExist(Film.class, filmId));
        actorRole = actorRoleRepository.save(actorRole);
        return translationService.toRead(actorRole);
    }
}