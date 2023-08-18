package com.example.supportticketddd.supportTicket.usecase.openSupportTicket

import com.example.supportticketddd.common.DomainEvent

class SupportTicketOpened extends DomainEvent{
    Long supportTicketId

    SupportTicketOpened(Object source, Long supportTicketId) {
        super(source)
        this.supportTicketId = supportTicketId
    }
}
