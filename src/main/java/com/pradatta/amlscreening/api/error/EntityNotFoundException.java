package com.pradatta.amlscreening.api.error;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Long id) {
        super("Could not find Sanctioned Entity " + id);
    }
}