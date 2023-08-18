package com.example.supportticketddd.supportTicket.usecase.replySupportTicket

import com.example.supportticketddd.common.DomainEventPublishHandler
import com.example.supportticketddd.member.entity.Member
import com.example.supportticketddd.member.entity.Role
import com.example.supportticketddd.supportTicket.entity.Level
import com.example.supportticketddd.supportTicket.entity.Status
import com.example.supportticketddd.supportTicket.entity.SupportTicket
import com.example.supportticketddd.supportTicket.entity.SupportTicketRecord
import com.example.supportticketddd.supportTicket.entity.TimeLimit
import com.example.supportticketddd.member.repository.MemberRepository
import com.example.supportticketddd.member.repository.memory.MemberInMemoryRepository
import com.example.supportticketddd.supportTicket.repository.SupportTicketRepository
import com.example.supportticketddd.supportTicket.repository.memory.SupportTicketInMemoryRepository
import com.example.supportticketddd.common.exception.ForbiddenMemberException
import com.example.supportticketddd.common.exception.ForbiddenStatusException
import spock.lang.Specification

class ReplySupportTicketCommandTest extends Specification {
    ReplySupportTicketCommandHandler commandHandler = new ReplySupportTicketCommandHandler(
            domainEventPublishHandler: Mock(DomainEventPublishHandler)
    )
    SupportTicketRepository supportTicketRepository
    MemberRepository memberRepository

    void setup() {
    }

    void cleanup() {
    }

    def "Happy Path"() {
        setup:
        def command = new ReplySupportTicketCommand(
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
                assignedOperatorId: 1,
        )
        mockSupportTicket.supportTicketRecordList = [
                new SupportTicketRecord(
                        id: 1,
                        supportTicket: mockSupportTicket,
                        content: "first",
                        posterId: 1
                )
        ]


        this.commandHandler.memberRepository = Mock(MemberRepository){
            findById(1) >> Optional.of(mockClient)
        }
        this.commandHandler.supportTicketRepository = Mock(SupportTicketRepository){
            findById(1) >> Optional.of(mockSupportTicket)
            save(_) >> 1
        }


        when:
        this.commandHandler.execute(command)

        then:
        with(this.commandHandler.supportTicketRepository){
            1 * save(_)
        }

    }
    def "Happy Path With In Memory Repository"() {
        setup:
        memberRepository = new MemberInMemoryRepository()
        supportTicketRepository = new SupportTicketInMemoryRepository()

        def command = new ReplySupportTicketCommand(
                supportTicketId: 1,
                content: "reply...",
                poster: 1
        )
        def mockClient = new Member(
                id: 1,
                name: "client",
                role: Role.CUSTOMER
        )
        def mockOperator = new Member(
                id: 2,
                name: "operator",
                role: Role.CUSTOMER_SERVICE_OPERATOR
        )
        def mockSupportTicket = new SupportTicket(
                id: 1,
                level: Level.valueOf("LOW"),
                timeLimit: TimeLimit.fromLevel(Level.valueOf("LOW")),
                title: "Test Support Ticket",
                status: Status.OPEN,
                customerId: 1,
                assignedOperatorId: 1,
        )
        mockSupportTicket.supportTicketRecordList = [
                new SupportTicketRecord(
                        id: 1,
                        supportTicket: mockSupportTicket,
                        content: "first",
                        posterId: 1
                )
        ]
        memberRepository.save(mockClient)
        memberRepository.save(mockOperator)
        supportTicketRepository.save(mockSupportTicket)
        commandHandler.memberRepository = memberRepository
        commandHandler.supportTicketRepository = supportTicketRepository


        when:
        def result = commandHandler.execute(command)

        then:
        result != null && result.id > 0

    }

    def "Customer Can Not Reply Other's Support Ticket"() {
        setup:
        memberRepository = new MemberInMemoryRepository()
        supportTicketRepository = new SupportTicketInMemoryRepository()

        def mockClientA = new Member(
                id: 1,
                name: "client",
                role: Role.CUSTOMER
        )
        def mockClientB = new Member(
                id: 2,
                name: "client",
                role: Role.CUSTOMER
        )
        def mockOperator = new Member(
                id: 3,
                name: "operator",
                role: Role.CUSTOMER_SERVICE_OPERATOR
        )
        def mockSupportTicketFromClientA = new SupportTicket(
                id: 1,
                level: Level.valueOf("LOW"),
                timeLimit: TimeLimit.fromLevel(Level.valueOf("LOW")),
                title: "Test Support Ticket From Client B",
                status: Status.OPEN,
                customerId: mockClientA.id,
                assignedOperatorId: mockOperator.id,
        )
        mockSupportTicketFromClientA.supportTicketRecordList = [
                new SupportTicketRecord(
                        id: 1,
                        supportTicket: mockSupportTicketFromClientA,
                        content: "todo...",
                        posterId: mockClientA.id
                )
        ]

        def mockSupportTicketFromClientB = new SupportTicket(
                id: 2,
                level: Level.valueOf("LOW"),
                timeLimit: TimeLimit.fromLevel(Level.valueOf("LOW")),
                title: "Test Support Ticket From Client B",
                status: Status.OPEN,
                customerId: mockClientB.id,
                assignedOperatorId: mockOperator.id,
        )
        mockSupportTicketFromClientB.supportTicketRecordList = [
                new SupportTicketRecord(
                        id: 2,
                        supportTicket: mockSupportTicketFromClientB,
                        content: "todo...",
                        posterId: mockClientB.id
                )
        ]

        memberRepository.save(mockClientA)
        memberRepository.save(mockClientB)
        memberRepository.save(mockOperator)
        supportTicketRepository.save(mockSupportTicketFromClientA)
        supportTicketRepository.save(mockSupportTicketFromClientB)
        commandHandler.memberRepository = memberRepository
        commandHandler.supportTicketRepository = supportTicketRepository

        def command = new ReplySupportTicketCommand(
                supportTicketId: mockSupportTicketFromClientA.id,
                content: "reply...",
                poster: mockClientB.id
        )

        when:
        def result = commandHandler.execute(command)

        then:
        def e = thrown(ForbiddenMemberException)
        e.message == "${mockClientB.getClass()}(${mockClientB.id}) not allowed to access"
    }


    def "Operator Should Reply Support Ticket Has Been Assigned"() {
        setup:
        memberRepository = new MemberInMemoryRepository()
        supportTicketRepository = new SupportTicketInMemoryRepository()

        def mockClient = new Member(
                id: 1,
                name: "client",
                role: Role.CUSTOMER
        )
        def mockOperatorA = new Member(
                id: 2,
                name: "operator",
                role: Role.CUSTOMER_SERVICE_OPERATOR
        )
        def mockOperatorB = new Member(
                id: 3,
                name: "operator",
                role: Role.CUSTOMER_SERVICE_OPERATOR
        )
        def mockSupportTicket = new SupportTicket(
                id: 1,
                level: Level.valueOf("LOW"),
                timeLimit: TimeLimit.fromLevel(Level.valueOf("LOW")),
                title: "Test Support Ticket From Client B",
                status: Status.OPEN,
                customerId: mockClient.id,
                assignedOperatorId: mockOperatorA.id,
        )
        mockSupportTicket.supportTicketRecordList = [
                new SupportTicketRecord(
                        id: 1,
                        supportTicket: mockSupportTicket,
                        content: "todo...",
                        posterId: mockClient.id
                )
        ]

        memberRepository.save(mockClient)
        memberRepository.save(mockOperatorA)
        memberRepository.save(mockOperatorB)
        supportTicketRepository.save(mockSupportTicket)
        commandHandler.memberRepository = memberRepository
        commandHandler.supportTicketRepository = supportTicketRepository

        def command = new ReplySupportTicketCommand(
                supportTicketId: mockSupportTicket.id,
                content: "reply...",
                poster: mockOperatorB.id
        )

        when:
        def result = commandHandler.execute(command)

        then:
        def e = thrown(ForbiddenMemberException)
        e.message == "${mockOperatorB.getClass()}(${mockOperatorB.id}) not allowed to access"
    }


    def "Not Allowed Reply [Closed] Support Ticket"() {
        setup:
        memberRepository = new MemberInMemoryRepository()
        supportTicketRepository = new SupportTicketInMemoryRepository()

        def mockClient = new Member(
                id: 1,
                name: "client",
                role: Role.CUSTOMER
        )
        def mockOperator = new Member(
                id: 2,
                name: "operator",
                role: Role.CUSTOMER_SERVICE_OPERATOR
        )
        def mockSupportTicket = new SupportTicket(
                id: 1,
                level: Level.valueOf("LOW"),
                timeLimit: TimeLimit.fromLevel(Level.valueOf("LOW")),
                title: "Test Support Ticket",
                status: Status.CLOSED,
                customerId: 1,
                assignedOperatorId: 1,
        )
        mockSupportTicket.supportTicketRecordList = [
                new SupportTicketRecord(
                        id: 1,
                        supportTicket: mockSupportTicket,
                        content: "first",
                        posterId: 1
                )
        ]
        memberRepository.save(mockClient)
        memberRepository.save(mockOperator)
        supportTicketRepository.save(mockSupportTicket)
        commandHandler.memberRepository = memberRepository
        commandHandler.supportTicketRepository = supportTicketRepository

        def command = new ReplySupportTicketCommand(
                supportTicketId: 1,
                content: "reply...",
                poster: 1
        )

        when:
        commandHandler.execute(command)

        then:
        thrown(ForbiddenStatusException)

    }

    def "Should Allowed Reply [Open|Reported] Support Ticket"() {
        setup:
        memberRepository = new MemberInMemoryRepository()
        supportTicketRepository = new SupportTicketInMemoryRepository()

        def mockClient = new Member(
                id: 1,
                name: "client",
                role: Role.CUSTOMER
        )
        def mockOperator = new Member(
                id: 2,
                name: "operator",
                role: Role.CUSTOMER_SERVICE_OPERATOR
        )
        def mockSupportTicket = new SupportTicket(
                id: 1,
                level: Level.valueOf("LOW"),
                timeLimit: TimeLimit.fromLevel(Level.valueOf("LOW")),
                title: "Test Support Ticket",
                status: actualStatus,
                customerId: 1,
                assignedOperatorId: 1,
        )
        mockSupportTicket.supportTicketRecordList = [
                new SupportTicketRecord(
                        id: 1,
                        supportTicket: mockSupportTicket,
                        content: "first",
                        posterId: 1
                )
        ]
        memberRepository.save(mockClient)
        memberRepository.save(mockOperator)
        supportTicketRepository.save(mockSupportTicket)
        commandHandler.memberRepository = memberRepository
        commandHandler.supportTicketRepository = supportTicketRepository

        def command = new ReplySupportTicketCommand(
                supportTicketId: 1,
                content: "reply...",
                poster: 1
        )

        when:
        commandHandler.execute(command)

        then:
        notThrown(ForbiddenStatusException)

        where:
        actualStatus | actual
        Status.OPEN | null
        Status.REPORTED | null
    }


}
