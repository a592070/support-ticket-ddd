package com.example.supportticketddd.supportTicket.usecase.replySupportTicket

import com.example.supportticketddd.common.DomainEvent

class SupportTicketReplied extends DomainEvent {
    Long supportTicketId

    SupportTicketReplied(Object source, Long supportTicketId) {
        super(source)
        this.supportTicketId = supportTicketId
    }
}
