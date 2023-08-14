package com.example.supportticketddd.entity.supportTicket

import com.example.supportticketddd.entity.AggregateRoot
import groovy.transform.ToString

import java.time.LocalDateTime

@ToString
class SupportTicket extends AggregateRoot{
    Long id
    LocalDateTime createDate
    LocalDateTime lastModifiedDate

    Level level
    TimeLimit timeLimit
    String title
    Status status

    Long customerId
    Long currentCustomerServiceOperatorId


    List<SupportTicketRecord> supportTicketRecordList = new ArrayList<SupportTicketRecord>()

}
