package com.example.supportticketddd.usecase

class ForbiddenStatusException extends RuntimeException{
    private final Object identifier
    private final String status

    ForbiddenStatusException(Object identifier, String status) {
        super("SupportTicket[id=${identifier}] NOT allowed to access: status=${status}")
        this.identifier = identifier
        this.status = status
    }
}
