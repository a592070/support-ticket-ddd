package com.example.supportticketddd.usecase.replySupportTicket

import com.example.supportticketddd.entity.member.Member
import com.example.supportticketddd.entity.member.Role
import com.example.supportticketddd.entity.supportTicket.Level
import com.example.supportticketddd.entity.supportTicket.Status
import com.example.supportticketddd.entity.supportTicket.SupportTicket
import com.example.supportticketddd.entity.supportTicket.SupportTicketRecord
import com.example.supportticketddd.entity.supportTicket.TimeLimit
import com.example.supportticketddd.repository.member.MemberRepository
import com.example.supportticketddd.repository.supportTicket.SupportTicketRepository
import spock.lang.Specification

class ReplySupportTicketCommandTest extends Specification {
    ReplySupportTicketCommand command = new ReplySupportTicketCommand()

    void setup() {
    }

    void cleanup() {
    }

    def "Happy Path"() {
        setup:
        def replySupportTicketDto = new ReplySupportTicketDto(
                supportTicketId: 1,
                content: "reply...",
                poster: 1
        )
        def mockClient = new Member(
                id: 1,
                name: "client",
                role: Role.CUSTOMER
        )
        def mockSupportTicket = new SupportTicket(
                id: 1,
                level: Level.valueOf("LOW"),
                timeLimit: TimeLimit.fromLevel(Level.valueOf("LOW")),
                title: "Test Support Ticket",
                status: Status.OPEN,
                customerId: 1,
                currentCustomerServiceOperatorId: 1,
        )
        mockSupportTicket.supportTicketRecordList = [
                new SupportTicketRecord(
                        id: 1,
                        supportTicket: mockSupportTicket,
                        content: "first",
                        posterId: 1
                )
        ]


        command.memberRepository = Mock(MemberRepository){
            findById(1) >> Optional.of(mockClient)
        }
        command.supportTicketRepository = Mock(SupportTicketRepository){
            findById(1) >> Optional.of(mockSupportTicket)
            save(_) >> 1
        }


        when:
        command.exec(replySupportTicketDto)

        then:
        with(command.supportTicketRepository){
            1 * save(_)
        }

    }
}
