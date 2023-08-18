package com.example.supportticketddd.supportTicket.usecase.replySupportTicket

import com.example.supportticketddd.common.CommandHandler
import com.example.supportticketddd.member.entity.Member
import com.example.supportticketddd.member.entity.Role
import com.example.supportticketddd.supportTicket.entity.Status
import com.example.supportticketddd.supportTicket.entity.SupportTicket
import com.example.supportticketddd.supportTicket.entity.SupportTicketRecord
import com.example.supportticketddd.member.repository.MemberRepository
import com.example.supportticketddd.supportTicket.repository.SupportTicketRepository
import com.example.supportticketddd.supportTicket.usecase.ForbiddenMemberException
import com.example.supportticketddd.supportTicket.usecase.ForbiddenStatusException
import com.example.supportticketddd.supportTicket.usecase.RepositoryEntityNotFoundException
import org.springframework.stereotype.Service

import javax.annotation.Resource

@Service
class ReplySupportTicketCommandHandler implements CommandHandler<ReplySupportTicketCommand, ReplySupportTicketCommandResult>{
    @Resource(name = "supportTicketJpaRepository")
    SupportTicketRepository supportTicketRepository

    @Resource(name = "memberJpaRepository")
    MemberRepository memberRepository

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

        return new ReplySupportTicketCommandResult(
                id: supportTicketRepository.save(supportTicket)
        )
    }

    private SupportTicket valid(Member poster, SupportTicket ticket) {
        if (poster.role == Role.CUSTOMER && poster.id != ticket.customerId) {
            throw new ForbiddenMemberException(poster.getClass(), poster.id)
        }
        if (poster.role == Role.CUSTOMER_SERVICE_OPERATOR && poster.id != ticket.assignedOperatorId) {
            throw new ForbiddenMemberException(poster.getClass(), poster.id)
        }
        if (ticket.status == Status.CLOSED) {
            throw new ForbiddenStatusException(ticket.id, ticket.status.toString())
        }
        return ticket
    }
}
