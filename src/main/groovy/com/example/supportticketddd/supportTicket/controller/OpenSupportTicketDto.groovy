package com.example.supportticketddd.supportTicket.controller

import com.example.supportticketddd.supportTicket.usecase.openSupportTicket.OpenSupportTicketCommand

class OpenSupportTicketDto {
    Long customerId
    String title
    String content
    String level

    OpenSupportTicketCommand toCommand(){
        return new OpenSupportTicketCommand(
                customerId: customerId,
                title: title,
                content: content,
                level: level
        )
    }
}
