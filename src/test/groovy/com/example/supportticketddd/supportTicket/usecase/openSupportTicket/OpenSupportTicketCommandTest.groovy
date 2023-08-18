package com.example.supportticketddd.supportTicket.usecase.openSupportTicket

import com.example.supportticketddd.member.entity.Member
import com.example.supportticketddd.member.entity.Role
import com.example.supportticketddd.member.repository.MemberRepository
import com.example.supportticketddd.member.repository.memory.MemberInMemoryRepository
import com.example.supportticketddd.supportTicket.repository.SupportTicketRepository
import com.example.supportticketddd.supportTicket.repository.memory.SupportTicketInMemoryRepository
import com.example.supportticketddd.supportTicket.usecase.RepositoryEntityNotFoundException
import spock.lang.Specification

class OpenSupportTicketCommandTest extends Specification {
    def command = new OpenSupportTicketCommand()
    SupportTicketRepository supportTicketRepository
    MemberRepository memberRepository

    def "Happy Path"() {
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
            pickupRandom(Role.CUSTOMER_SERVICE_OPERATOR) >> Optional.of(new Member(
                    id: 2,
                    name: "operator",
                    role: Role.CUSTOMER_SERVICE_OPERATOR
            ))
        }
        command.supportTicketRepository = Mock(SupportTicketRepository){
            save(_) >> 1
        }

        when:
        def result = command.exec(ticketDto)

        then:
        with(command.supportTicketRepository){
            1 * save(_)
        }
    }

    def "Happy Path With In Memory Repository"() {
        setup:
        memberRepository = new MemberInMemoryRepository()
        supportTicketRepository = new SupportTicketInMemoryRepository()

        memberRepository.save(
                new Member(
                        id: 1,
                        name: "client",
                        role: Role.CUSTOMER
                )
        )
        memberRepository.save(
                new Member(
                        id: 2,
                        name: "operator",
                        role: Role.CUSTOMER_SERVICE_OPERATOR
                )
        )
        command.memberRepository = memberRepository
        command.supportTicketRepository = supportTicketRepository

        def ticketDto = new OpenSupportTicketDto(
                customerId: 1,
                title: "Test Ticket",
                content: "something todo...",
                level: "LOW"
        )

        when:
        def result = command.exec(ticketDto)

        then:
        result != null

    }

    def "Customer Not Exist"(){
        setup:
        def ticketDto = new OpenSupportTicketDto(
                customerId: 1,
                title: "Test Ticket",
                content: "something todo...",
                level: "LOW"
        )
        command.memberRepository = Mock(MemberRepository){
            findById(1) >> Optional.empty()
        }

        when:
        def result = command.exec(ticketDto)

        then:
        thrown(RepositoryEntityNotFoundException)
    }


    def "Operator Not Exist"(){
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
            pickupRandom(Role.CUSTOMER_SERVICE_OPERATOR) >> Optional.empty()
        }

        when:
        def result = command.exec(ticketDto)

        then:
        thrown(RepositoryEntityNotFoundException)
    }

    def "Illegal Params: level"(){
        setup:
        def ticketDto = new OpenSupportTicketDto(
                customerId: 1,
                title: "Test Ticket",
                content: "something todo...",
                level: "Unknown"
        )
        command.memberRepository = Mock(MemberRepository){
            findById(1) >> Optional.of(new Member(
                    id: 1,
                    name: "client",
                    role: Role.CUSTOMER
            ))
            pickupRandom(Role.CUSTOMER_SERVICE_OPERATOR) >> Optional.of(new Member(
                    id: 2,
                    name: "operator",
                    role: Role.CUSTOMER_SERVICE_OPERATOR
            ))
        }
        command.supportTicketRepository = Mock(SupportTicketRepository){
            save(_) >> 1
        }

        when:
        def result = command.exec(ticketDto)

        then:
        thrown(IllegalArgumentException)

    }


}
