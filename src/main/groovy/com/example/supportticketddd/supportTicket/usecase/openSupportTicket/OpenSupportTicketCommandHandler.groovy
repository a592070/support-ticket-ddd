package com.example.supportticketddd.supportTicket.usecase.openSupportTicket


import com.example.supportticketddd.common.CommandHandler
import com.example.supportticketddd.common.DomainEventPublishHandler
import com.example.supportticketddd.member.entity.Customer
import com.example.supportticketddd.member.entity.CustomerServiceOperator
import com.example.supportticketddd.member.entity.Role
import com.example.supportticketddd.supportTicket.entity.Level
import com.example.supportticketddd.supportTicket.entity.Status
import com.example.supportticketddd.supportTicket.entity.SupportTicket
import com.example.supportticketddd.supportTicket.entity.SupportTicketRecord
import com.example.supportticketddd.supportTicket.entity.TimeLimit
import com.example.supportticketddd.member.repository.MemberRepository
import com.example.supportticketddd.supportTicket.repository.SupportTicketRepository
import com.example.supportticketddd.common.exception.RepositoryEntityNotFoundException
import org.springframework.stereotype.Service

import javax.annotation.Resource
import java.time.LocalDateTime

@Service
class OpenSupportTicketCommandHandler implements CommandHandler<OpenSupportTicketCommand, OpenSupportTicketCommandResult> {

    @Resource(name = "memberJpaRepository")
    MemberRepository memberRepository

    @Resource(name = "supportTicketJpaRepository")
    SupportTicketRepository supportTicketRepository

    @Resource
    DomainEventPublishHandler domainEventPublishHandler

    @Override
    OpenSupportTicketCommandResult execute(OpenSupportTicketCommand openSupportTicket){
        def customer = memberRepository
                .findById(openSupportTicket.customerId)
                .orElseThrow {
                    new RepositoryEntityNotFoundException(Customer, openSupportTicket.customerId)
                }
        def customerServiceOperator = memberRepository
                .pickupRandom(Role.CUSTOMER_SERVICE_OPERATOR)
                .orElseThrow {
                    new RepositoryEntityNotFoundException(CustomerServiceOperator)
                }


        def level = Level.valueOf(openSupportTicket.level)
        def supportTicket = new SupportTicket(
                level: level,
                timeLimit: TimeLimit.fromLevel(level),
                createDate: LocalDateTime.now(),
                lastModifiedDate: LocalDateTime.now(),
                title: openSupportTicket.title,
                status: Status.OPEN,
                customerId: customer.id,
                assignedOperatorId: customerServiceOperator.id
        )
        supportTicket.addSupportTicketRecord(
                new SupportTicketRecord(
                        content: openSupportTicket.content,
                        posterId: openSupportTicket.customerId,
                )
        )
        def id = supportTicketRepository.save(supportTicket)


        supportTicket.addDomainEvents(new SupportTicketOpened(
                this, id
        ))

        domainEventPublishHandler.publishAll(supportTicket)

        return new OpenSupportTicketCommandResult(
                id: id
        )
    }
}
