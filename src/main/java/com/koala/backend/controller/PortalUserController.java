package com.koala.backend.controller;

import com.koala.backend.dto.portaluser.PortalUserCreateDTO;
import com.koala.backend.dto.portaluser.PortalUserPatchDTO;
import com.koala.backend.dto.portaluser.PortalUserReadDTO;
import com.koala.backend.service.PortalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/portalUsers")
public class PortalUserController {

    @Autowired
    private PortalUserService portalUserService;

    @GetMapping("/{id}")
    public PortalUserReadDTO getPortalUser(@PathVariable UUID id) {
        return portalUserService.getPortalUser(id);
    }

    @PostMapping
    public PortalUserReadDTO createPortalUser(@RequestBody PortalUserCreateDTO createDTO) {
        return portalUserService.createPortalUser(createDTO);
    }

    @PatchMapping("/{id}")
    public PortalUserReadDTO patchPortalUser(@PathVariable UUID id, @RequestBody PortalUserPatchDTO patch) {
        return portalUserService.patchPortalUser(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deletePortalUser(@PathVariable UUID id) {
        portalUserService.deletePortalUser(id);
    }

}
