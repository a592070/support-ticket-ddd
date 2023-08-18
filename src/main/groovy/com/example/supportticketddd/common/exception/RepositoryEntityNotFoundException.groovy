package com.example.supportticketddd.common.exception

import javax.persistence.EntityNotFoundException

class RepositoryEntityNotFoundException extends EntityNotFoundException{

    private final Class entity;
    private final Object identifier;


    RepositoryEntityNotFoundException(Class entity) {
        super("Entity ${entity} does not exist");
        this.entity = entity;
    }

    RepositoryEntityNotFoundException(Class entity, Object identifier) {
        super("Entity ${entity} with identifier value ${identifier} does not exist");
        this.entity = entity;
        this.identifier = identifier;
    }
}