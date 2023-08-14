package com.example.supportticketddd.usecase.replySupportTicket

import com.example.supportticketddd.entity.member.Member
import com.example.supportticketddd.entity.supportTicket.SupportTicket
import com.example.supportticketddd.entity.supportTicket.SupportTicketRecord
import com.example.supportticketddd.repository.member.MemberRepository
import com.example.supportticketddd.repository.supportTicket.SupportTicketRepository
import com.example.supportticketddd.usecase.RepositoryEntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ReplySupportTicketCommand {
    @Autowired
    SupportTicketRepository supportTicketRepository

    @Autowired
    MemberRepository memberRepository

    Long exec(ReplySupportTicketDto replySupportTicketDto){

        def supportTicket = supportTicketRepository
                .findById(replySupportTicketDto.supportTicketId)
                .orElseThrow {
                    new RepositoryEntityNotFoundException(SupportTicket, replySupportTicketDto.supportTicketId)
                }

        def poster = memberRepository
                .findById(replySupportTicketDto.poster)
                .orElseThrow {
                    new RepositoryEntityNotFoundException(Member, replySupportTicketDto.poster)
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
