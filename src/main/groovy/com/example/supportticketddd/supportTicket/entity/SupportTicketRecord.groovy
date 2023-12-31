package com.example.supportticketddd.supportTicket.entity

import groovy.transform.EqualsAndHashCode

import java.time.LocalDateTime

@EqualsAndHashCode(includes = "id")
class SupportTicketRecord {
    Long id
    LocalDateTime createDate
    LocalDateTime lastModifiedDate

    SupportTicket supportTicket
    String content

    Long posterId
}
