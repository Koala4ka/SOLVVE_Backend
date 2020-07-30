package com.koala.backend.dto.portaluser;

import lombok.Data;

import java.util.UUID;

@Data
public class PortalUserReadDTO {

    private UUID id;
    private String name;
    private String country;

    private Integer age;
}
