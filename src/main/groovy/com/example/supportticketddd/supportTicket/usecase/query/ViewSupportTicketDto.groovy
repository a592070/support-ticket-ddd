package com.example.supportticketddd.supportTicket.usecase.query


import java.time.LocalDateTime

class ViewSupportTicketDto {
    long id
    LocalDateTime createDate
    LocalDateTime lastModifiedDate

    String level
    Long timeLimit
    String title
    String status


    List<SupportTicketRecord> supportTicketRecordList
    Member customer
    Member assignedOperator
}
class SupportTicketRecord{
    long id
    String content
    Member poster
}

class Member{
    long id
    String name
    String role
}
