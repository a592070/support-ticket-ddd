package com.example.supportticketddd.supportTicket.usecase.replySupportTicket

import com.example.supportticketddd.common.CommandHandler
import com.example.supportticketddd.common.DomainEventPublishHandler
import com.example.supportticketddd.member.entity.Member
import com.example.supportticketddd.member.entity.Role
import com.example.supportticketddd.supportTicket.entity.Status
import com.example.supportticketddd.supportTicket.entity.SupportTicket
import com.example.supportticketddd.supportTicket.entity.SupportTicketRecord
import com.example.supportticketddd.member.repository.MemberRepository
import com.example.supportticketddd.supportTicket.repository.SupportTicketRepository
import com.example.supportticketddd.common.exception.ForbiddenMemberException
import com.example.supportticketddd.common.exception.ForbiddenStatusException
import com.example.supportticketddd.common.exception.RepositoryEntityNotFoundException
import org.springframework.stereotype.Service

import javax.annotation.Resource

@Service
class ReplySupportTicketCommandHandler implements CommandHandler<ReplySupportTicketCommand, ReplySupportTicketCommandResult>{
    @Resource(name = "supportTicketJpaRepository")
    SupportTicketRepository supportTicketRepository

    @Resource(name = "memberJpaRepository")
    MemberRepository memberRepository

    @Resource
    DomainEventPublishHandler domainEventPublishHandler

    @Override
    ReplySupportTicketCommandResult execute(ReplySupportTicketCommand command){

        def poster = memberRepository
                .findById(command.poster)
                .orElseThrow {
                    new RepositoryEntityNotFoundException(Member, command.poster)
                }

        def supportTicket = supportTicketRepository
                .findById(command.supportTicketId)
                .map { ticket ->
                    return valid(poster, ticket)
                }
                .orElseThrow {
                    new RepositoryEntityNotFoundException(SupportTicket, command.supportTicketId)
                }


        supportTicket.addSupportTicketRecord(
                new SupportTicketRecord(
                        posterId: poster.id,
                        content: command.content
                )
        )

        def id = supportTicketRepository.save(supportTicket)


        supportTicket.addDomainEvents(
                new SupportTicketReplied(this, id)
        )

        domainEventPublishHandler.publishAll(supportTicket)


        return new ReplySupportTicketCommandResult(
                id: id
        )
    }

    private SupportTicket valid(Member poster, SupportTicket ticket) {
        if (poster.isCustomer() && poster.id != ticket.customerId) {
            throw new ForbiddenMemberException(poster.getClass(), poster.id)
        }
        if (poster.isOperator() && poster.id != ticket.assignedOperatorId) {
            throw new ForbiddenMemberException(poster.getClass(), poster.id)
        }
        if (ticket.isClosed()) {
            throw new ForbiddenStatusException(ticket.id, ticket.status.toString())
        }
        return ticket
    }

}
