package com.example.supportticketddd.usecase

class ForbiddenMemberException extends RuntimeException{
    private final Class entity;
    private final Object identifier;

    ForbiddenMemberException(Class entity) {
        super("${entity} not allowed to access");
        this.entity = entity;
    }

    ForbiddenMemberException(Class entity, Object identifier) {
        super("${entity}(${identifier}) not allowed to access");
        this.entity = entity;
        this.identifier = identifier;
    }
}
