package com.example.supportticketddd.supportTicket.usecase.replySupportTicket

import com.example.supportticketddd.common.Command

class ReplySupportTicketCommand implements Command{
    Long supportTicketId
    String content
    Long poster
}
