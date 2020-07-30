package com.koala.backend.dto.actorrole;

import lombok.Data;

import java.util.UUID;

@Data
public class ActorRoleReadDTO {

    private UUID id;
    private UUID filmId;

}
