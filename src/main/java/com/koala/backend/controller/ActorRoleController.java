package com.koala.backend.controller;

import com.koala.backend.dto.actorrole.ActorRoleReadDTO;
import com.koala.backend.service.ActorRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/films/{filmId}/actorRoles")
public class ActorRoleController {

    @Autowired
    private ActorRoleService actorRoleService;

    @GetMapping
    public List<ActorRoleReadDTO> getFilmRoles(@PathVariable UUID filmId) {

        return actorRoleService.getFilmRoles(filmId);
    }
}
