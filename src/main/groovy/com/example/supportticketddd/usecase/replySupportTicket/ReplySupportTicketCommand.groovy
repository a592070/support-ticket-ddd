package com.example.supportticketddd.usecase.replySupportTicket

import com.example.supportticketddd.entity.member.Member
import com.example.supportticketddd.entity.member.Role
import com.example.supportticketddd.entity.supportTicket.Status
import com.example.supportticketddd.entity.supportTicket.SupportTicket
import com.example.supportticketddd.entity.supportTicket.SupportTicketRecord
import com.example.supportticketddd.repository.member.MemberRepository
import com.example.supportticketddd.repository.supportTicket.SupportTicketRepository
import com.example.supportticketddd.usecase.ForbiddenMemberException
import com.example.supportticketddd.usecase.ForbiddenStatusException
import com.example.supportticketddd.usecase.RepositoryEntityNotFoundException
import org.springframework.stereotype.Service

import javax.annotation.Resource

@Service
class ReplySupportTicketCommand {
    @Resource(name = "supportTicketJpaRepository")
    SupportTicketRepository supportTicketRepository

    @Resource(name = "memberJpaRepository")
    MemberRepository memberRepository

    Long exec(ReplySupportTicketDto replySupportTicketDto){

        def poster = memberRepository
                .findById(replySupportTicketDto.poster)
                .orElseThrow {
                    new RepositoryEntityNotFoundException(Member, replySupportTicketDto.poster)
                }

        def supportTicket = supportTicketRepository
                .findById(replySupportTicketDto.supportTicketId)
                .map { ticket ->
                    if (poster.role == Role.CUSTOMER && poster.id != ticket.customerId){
                        throw new ForbiddenMemberException(poster.getClass(), poster.id)
                    }
                    if (poster.role == Role.CUSTOMER_SERVICE_OPERATOR && poster.id != ticket.assignedOperatorId){
                        throw new ForbiddenMemberException(poster.getClass(), poster.id)
                    }
                    if (ticket.status == Status.CLOSED){
                        throw new ForbiddenStatusException(ticket.id, ticket.status.toString())
                    }
                    return ticket
                }
                .orElseThrow {
                    new RepositoryEntityNotFoundException(SupportTicket, replySupportTicketDto.supportTicketId)
                }


        supportTicket.addSupportTicketRecord(
                new SupportTicketRecord(
                        posterId: poster.id,
                        content: replySupportTicketDto.content
                )
        )

        return supportTicketRepository.save(supportTicket)
    }
}
