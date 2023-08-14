package com.example.supportticketddd.usecase.openSupportTicket

import com.example.supportticketddd.entity.member.Member
import com.example.supportticketddd.entity.member.Role
import com.example.supportticketddd.repository.member.MemberRepository
import com.example.supportticketddd.repository.member.memory.MemberInMemoryRepository
import com.example.supportticketddd.repository.supportTicket.SupportTicketRepository
import com.example.supportticketddd.repository.supportTicket.memory.SupportTicketInMemoryRepository
import com.example.supportticketddd.usecase.RepositoryEntityNotFoundException
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
        result != null

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
