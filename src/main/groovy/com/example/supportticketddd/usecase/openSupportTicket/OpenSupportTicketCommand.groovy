package com.example.supportticketddd.usecase.openSupportTicket

import com.example.supportticketddd.entity.member.Customer
import com.example.supportticketddd.entity.member.CustomerServiceOperator
import com.example.supportticketddd.entity.member.Role
import com.example.supportticketddd.entity.supportTicket.Level
import com.example.supportticketddd.entity.supportTicket.Status
import com.example.supportticketddd.entity.supportTicket.SupportTicket
import com.example.supportticketddd.entity.supportTicket.SupportTicketRecord
import com.example.supportticketddd.entity.supportTicket.TimeLimit
import com.example.supportticketddd.repository.member.MemberRepository
import com.example.supportticketddd.repository.supportTicket.SupportTicketRepository
import com.example.supportticketddd.usecase.RepositoryEntityNotFoundException
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
                currentCustomerServiceOperatorId: customerServiceOperator.id
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
