package com.example.supportticketddd.usecase.query

import com.example.supportticketddd.entity.supportTicket.SupportTicket
import com.example.supportticketddd.repository.supportTicket.SupportTicketRepositoryPeer
import com.example.supportticketddd.usecase.RepositoryEntityNotFoundException
import org.springframework.stereotype.Service

import javax.annotation.Resource

@Service
class ViewSupportTicketQuery {


    @Resource
    SupportTicketRepositoryPeer supportTicketRepositoryPeer


    ViewSupportTicketDto exec(long id){
        def supportTicketData = supportTicketRepositoryPeer
                .findById(id)
                .orElseThrow {
                    new RepositoryEntityNotFoundException(SupportTicket, id)
                }

        return new ViewSupportTicketDto(
                id: supportTicketData.id,
                createDate: supportTicketData.createDate,
                lastModifiedDate: supportTicketData.lastModifiedDate,
                level: supportTicketData.level,
                timeLimit: supportTicketData.timeLimit,
                title: supportTicketData.title,
                status: supportTicketData.status,
                customer: new Member(
                        id: supportTicketData.customer.id,
                        name: supportTicketData.customer.name,
                        role: supportTicketData.customer.role
                ),
                currentCustomerServiceOperator: new Member(
                        id: supportTicketData.currentCustomerServiceOperator.id,
                        name: supportTicketData.currentCustomerServiceOperator.name,
                        role: supportTicketData.currentCustomerServiceOperator.role
                ),
                supportTicketRecordList: supportTicketData.supportTicketRecordDataList.collect {
                    new SupportTicketRecord(
                            id: it.id,
                            content: it.content,
                            poster: new Member(
                                    id: it.poster.id,
                                    name: it.poster.name,
                                    role: it.poster.role
                            )
                    )
                }
        )
    }

}
