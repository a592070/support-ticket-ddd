package com.example.supportticketddd.supportTicket.usecase.openSupportTicket

import com.example.supportticketddd.common.Command

class OpenSupportTicketCommand implements Command{
    Long customerId
    String title
    String content
    String level
}
