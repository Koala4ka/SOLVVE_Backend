package com.koala.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class entityClass, UUID id) {
        super(String.format("Entity %s with id=%s is not found!", entityClass.getSimpleName(), id));
    }
}
