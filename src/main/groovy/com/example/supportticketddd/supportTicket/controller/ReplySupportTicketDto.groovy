package com.example.supportticketddd.supportTicket.controller

import com.example.supportticketddd.supportTicket.usecase.replySupportTicket.ReplySupportTicketCommand


class ReplySupportTicketDto {
    Long supportTicketId
    String content
    Long poster

    ReplySupportTicketCommand toCommand(){
        return new ReplySupportTicketCommand(
                supportTicketId: supportTicketId,
                content: content,
                poster: poster,
        )
    }
}
