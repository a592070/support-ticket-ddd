package com.example.supportticketddd.usecase.openSupportTicket

import javax.persistence.EntityNotFoundException

class RepositoryEntityNotFoundException extends EntityNotFoundException{

    private final Class entity;
    private final Object identifier;


    RepositoryEntityNotFoundException(Class entity) {
        super(
                String.format(
                        Locale.ROOT,
                        "Entity `%s` does not exist",
                        entity,
                )
        );
        this.entity = entity;
    }

    RepositoryEntityNotFoundException(Class entity, Object identifier) {
        super(
                String.format(
                        Locale.ROOT,
                        "Entity `%s` with identifier value `%s` does not exist",
                        entity,
                        identifier
                )
        );
        this.entity = entity;
        this.identifier = identifier;
    }
}