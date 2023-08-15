package com.example.supportticketddd.entity.supportTicket

import com.example.supportticketddd.entity.AggregateRoot
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import java.time.LocalDateTime

@ToString
@EqualsAndHashCode(includes = "id")
class SupportTicket extends AggregateRoot{
    Long id
    LocalDateTime createDate
    LocalDateTime lastModifiedDate

    Level level
    TimeLimit timeLimit
    String title
    Status status

    Long customerId
    Long assignedOperatorId


    List<SupportTicketRecord> supportTicketRecordList = new ArrayList<SupportTicketRecord>()


    void addSupportTicketRecord(SupportTicketRecord supportTicketRecord){
        supportTicketRecord.supportTicket = this
        supportTicketRecordList.add(supportTicketRecord)
    }

}
