package com.example.supportticketddd.supportTicket.usecase.openSupportTicket

import com.example.supportticketddd.common.DomainEventPublishHandler
import com.example.supportticketddd.member.entity.Member
import com.example.supportticketddd.member.entity.Role
import com.example.supportticketddd.member.repository.MemberRepository
import com.example.supportticketddd.member.repository.memory.MemberInMemoryRepository
import com.example.supportticketddd.supportTicket.repository.SupportTicketRepository
import com.example.supportticketddd.supportTicket.repository.memory.SupportTicketInMemoryRepository
import com.example.supportticketddd.common.exception.RepositoryEntityNotFoundException
import spock.lang.Specification

class OpenSupportTicketCommandTest extends Specification {
    def commandHandler = new OpenSupportTicketCommandHandler(
            domainEventPublishHandler: Mock(DomainEventPublishHandler)
    )
    SupportTicketRepository supportTicketRepository
    MemberRepository memberRepository

    def "Happy Path"() {
        setup:
        def command = new OpenSupportTicketCommand(
                customerId: 1,
                title: "Test Ticket",
                content: "something todo...",
                level: "LOW"
        )
        commandHandler.memberRepository = Mock(MemberRepository){
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
        commandHandler.supportTicketRepository = Mock(SupportTicketRepository){
            save(_) >> 1
        }

        when:
        def result = commandHandler.execute(command)

        then:
        with(commandHandler.supportTicketRepository){
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
        commandHandler.memberRepository = memberRepository
        commandHandler.supportTicketRepository = supportTicketRepository

        def command = new OpenSupportTicketCommand(
                customerId: 1,
                title: "Test Ticket",
                content: "something todo...",
                level: "LOW"
        )

        when:
        def result = commandHandler.execute(command)

        then:
        result != null

    }

    def "Customer Not Exist"(){
        setup:
        def command = new OpenSupportTicketCommand(
                customerId: 1,
                title: "Test Ticket",
                content: "something todo...",
                level: "LOW"
        )
        commandHandler.memberRepository = Mock(MemberRepository){
            findById(1) >> Optional.empty()
        }

        when:
        def result = commandHandler.execute(command)

        then:
        thrown(RepositoryEntityNotFoundException)
    }


    def "Operator Not Exist"(){
        setup:
        def command = new OpenSupportTicketCommand(
                customerId: 1,
                title: "Test Ticket",
                content: "something todo...",
                level: "LOW"
        )
        commandHandler.memberRepository = Mock(MemberRepository){
            findById(1) >> Optional.of(new Member(
                    id: 1,
                    name: "client",
                    role: Role.CUSTOMER
            ))
            pickupRandom(Role.CUSTOMER_SERVICE_OPERATOR) >> Optional.empty()
        }

        when:
        def result = commandHandler.execute(command)

        then:
        thrown(RepositoryEntityNotFoundException)
    }

    def "Illegal Params: level"(){
        setup:
        def command = new OpenSupportTicketCommand(
                customerId: 1,
                title: "Test Ticket",
                content: "something todo...",
                level: "Unknown"
        )
        commandHandler.memberRepository = Mock(MemberRepository){
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
        commandHandler.supportTicketRepository = Mock(SupportTicketRepository){
            save(_) >> 1
        }

        when:
        def result = commandHandler.execute(command)

        then:
        thrown(IllegalArgumentException)

    }


}
