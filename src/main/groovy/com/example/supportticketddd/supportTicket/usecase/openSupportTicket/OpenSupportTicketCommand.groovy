package com.example.supportticketddd.supportTicket.usecase.openSupportTicket

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
import com.example.supportticketddd.supportTicket.usecase.RepositoryEntityNotFoundException
import org.springframework.stereotype.Service

import javax.annotation.Resource
import java.time.LocalDateTime

@Service
class OpenSupportTicketCommand {

    @Resource(name = "memberJpaRepository")
    MemberRepository memberRepository

    @Resource(name = "supportTicketJpaRepository")
    SupportTicketRepository supportTicketRepository

    Long exec(OpenSupportTicketDto openSupportTicketDto){
        def customer = memberRepository
                .findById(openSupportTicketDto.customerId)
                .orElseThrow {
                    new RepositoryEntityNotFoundException(Customer, openSupportTicketDto.customerId)
                }
        def customerServiceOperator = memberRepository
                .pickupRandom(Role.CUSTOMER_SERVICE_OPERATOR)
                .orElseThrow {
                    new RepositoryEntityNotFoundException(CustomerServiceOperator)
                }


        def level = Level.valueOf(openSupportTicketDto.level)
        def supportTicket = new SupportTicket(
                level: level,
                timeLimit: TimeLimit.fromLevel(level),
                createDate: LocalDateTime.now(),
                lastModifiedDate: LocalDateTime.now(),
                title: openSupportTicketDto.title,
                status: Status.OPEN,
                customerId: customer.id,
                assignedOperatorId: customerServiceOperator.id
        )
        supportTicket.supportTicketRecordList.add(new SupportTicketRecord(
                supportTicket: supportTicket,
                createDate: LocalDateTime.now(),
                lastModifiedDate: LocalDateTime.now(),
                content: openSupportTicketDto.content,
                posterId: openSupportTicketDto.customerId,
        ))

        supportTicketRepository.save(supportTicket)
    }

}
