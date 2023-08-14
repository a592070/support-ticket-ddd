package com.example.supportticketddd.usecase.openSupportTicket

import com.example.supportticketddd.entity.member.Member
import com.example.supportticketddd.entity.member.Role
import com.example.supportticketddd.repository.member.MemberRepository
import com.example.supportticketddd.repository.supportTicket.SupportTicketRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

class OpenSupportTicketCommandTest extends Specification {
    def command = new OpenSupportTicketCommand()

    def "happy path"() {
        setup:
        def ticketDto = new OpenSupportTicketDto(
                customerId: 1,
                title: "Test Ticket",
                content: "something todo...",
                level: "LOW"
        )
        command.memberRepository = Mock(MemberRepository){
            findById(1) >> Optional.of(new Member(
                    id: 1,
                    name: "client",
                    role: Role.CUSTOMER
            ))
            pickupRandom(Role.CUSTOMER_SERVICE_OPERATOR) >> new Member(
                    id: 2,
                    name: "operator",
                    role: Role.CUSTOMER_SERVICE_OPERATOR
            )
        }
        command.supportTicketRepository = Mock(SupportTicketRepository){
            save(_) >> 1
        }

        when:
        def result = command.exec(ticketDto)

        then:
        result != null

    }
}
