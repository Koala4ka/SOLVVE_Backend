package com.koala.backend.service;

import com.koala.backend.domain.PortalUser;
import com.koala.backend.dto.portaluser.PortalUserCreateDTO;
import com.koala.backend.dto.portaluser.PortalUserPatchDTO;
import com.koala.backend.dto.portaluser.PortalUserPutDTO;
import com.koala.backend.dto.portaluser.PortalUserReadDTO;
import com.koala.backend.exception.EntityNotFoundException;
import com.koala.backend.repository.PortalUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PortalUserService {

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private TranslationService translationService;

    public PortalUserReadDTO getPortalUser(UUID id) {

        PortalUser portalUser = getPortalUserRequired(id);
        return toRead(portalUser);
    }

    public PortalUserReadDTO createPortalUser(PortalUserCreateDTO create) {

        PortalUser portalUser = translationService.toEntity(create);

        portalUser = portalUserRepository.save(portalUser);
        return translationService.toRead(portalUser);
    }

    public PortalUserReadDTO patchPortalUser(UUID id, PortalUserPatchDTO patch) {
        PortalUser portalUser = getPortalUserRequired(id);

        translationService.patchEntity(patch, portalUser);

        portalUser = portalUserRepository.save(portalUser);
        return translationService.toRead(portalUser);
    }

    public void deletePortalUser(UUID id) {
        portalUserRepository.delete(getPortalUserRequired(id));
    }

    public PortalUser getPortalUserRequired(UUID id) {
        return portalUserRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(PortalUser.class, id)
        );

    }

    private PortalUserReadDTO toRead(PortalUser portalUser) {

        PortalUserReadDTO dto = new PortalUserReadDTO();
        dto.setId(portalUser.getId());
        dto.setName(portalUser.getName());
        dto.setCountry(portalUser.getCountry());
        dto.setAge(portalUser.getAge());
        return dto;
    }

    public PortalUserReadDTO updatePortalUser(UUID id, PortalUserPutDTO update) {

        PortalUser portalUser = getPortalUserRequired(id);

        portalUser.setName(update.getName());
        portalUser.setCountry(update.getCountry());
        portalUser.setAge(update.getAge());

        portalUser = portalUserRepository.save(portalUser);

        return translationService.toRead(portalUser);
    }
}
