package com.example.supportticketddd.entity.supportTicket


import java.time.LocalDateTime

class SupportTicketRecord {
    Long id
    LocalDateTime createDate
    LocalDateTime lastModifiedDate

    SupportTicket supportTicket
    String content

    Long posterId
}
