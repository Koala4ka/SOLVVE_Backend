package com.koala.backend.dto.portaluser;

import lombok.Data;

@Data
public class PortalUserPatchDTO {

    private String name;
    private String country;

    private Integer age;
}
