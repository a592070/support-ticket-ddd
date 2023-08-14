package com.example.supportticketddd.repository.supportTicket.jpa

import com.example.supportticketddd.entity.supportTicket.Level
import com.example.supportticketddd.entity.supportTicket.Status
import com.example.supportticketddd.entity.supportTicket.SupportTicket
import com.example.supportticketddd.entity.supportTicket.SupportTicketRecord
import com.example.supportticketddd.entity.supportTicket.TimeLimit
import com.example.supportticketddd.repository.member.jpa.MemberRepositoryPeer
import com.example.supportticketddd.repository.supportTicket.SupportTicketRepository
import org.springframework.stereotype.Repository

import javax.annotation.Resource
import javax.transaction.Transactional

@Repository
@Transactional
class SupportTicketJpaRepository implements SupportTicketRepository{
    @Resource
    SupportTicketRepositoryPeer supportTicketRepositoryPeer

    @Resource
    MemberRepositoryPeer memberRepositoryPeer

    @Override
    List<SupportTicket> findAll() {
        return supportTicketRepositoryPeer.findAll().collect {data ->
            convert(data)
        }
    }

    @Override
    Optional<SupportTicket> findById(Long id) {
        return supportTicketRepositoryPeer.findById(id).map {data ->
            convert(data)
        }
    }

    @Override
    Long save(SupportTicket supportTicket) {
        def supportTicketData
        if(!supportTicket.id || !supportTicketRepositoryPeer.existsById(supportTicket.id)){
            supportTicketData = new SupportTicketData(
                    level: supportTicket.level.toString(),
                    timeLimit: supportTicket.timeLimit.value(),
                    title: supportTicket.title,
                    status: supportTicket.status.toString(),
            )
        }else{
            supportTicketData = supportTicketRepositoryPeer.getReferenceById(supportTicket.id)
        }
        def customer = memberRepositoryPeer.getReferenceById(supportTicket.customerId)
        def operator = memberRepositoryPeer.getReferenceById(supportTicket.currentCustomerServiceOperatorId)

        supportTicketData.customer = customer
        supportTicketData.currentCustomerServiceOperator = operator
        supportTicketData.supportTicketRecordDataList = supportTicket.supportTicketRecordList.collect {supportTicketRecord ->
            def member = memberRepositoryPeer.getReferenceById(supportTicketRecord.posterId)
            new SupportTicketRecordData(
                    content: supportTicketRecord.content,
                    poster: member,
                    supportTicketData: supportTicketData
            )
        }


        supportTicketRepositoryPeer.save(supportTicketData).id
    }

    @Override
    void deleteById(Long id) {
        supportTicketRepositoryPeer.deleteById(id)
    }

    SupportTicket convert(SupportTicketData supportTicketData){
        def supportTicket = new SupportTicket(
                id: supportTicketData.id,
                level: Level.valueOf(supportTicketData.level),
                timeLimit: TimeLimit.valueOf(supportTicketData.timeLimit),
                title: supportTicketData.title,
                customerId: supportTicketData.customer.id,
                currentCustomerServiceOperatorId: supportTicketData.currentCustomerServiceOperator.id,
                status: Status.valueOf(supportTicketData.status)
        )
        supportTicket.supportTicketRecordList = supportTicketData.supportTicketRecordDataList.collect {supportTicketRecordData ->
            return new SupportTicketRecord(
                    id: supportTicketRecordData.id,
                    createDate: supportTicketRecordData.createDate,
                    lastModifiedDate: supportTicketRecordData.lastModifiedDate,
                    content: supportTicketRecordData.content,
                    supportTicket: supportTicket,
                    posterId: supportTicketRecordData.poster.id
            )
        }

        return supportTicket
    }

    SupportTicketData convert(SupportTicket supportTicket){
        def customer = memberRepositoryPeer.getReferenceById(supportTicket.customerId)
        def operator = memberRepositoryPeer.getReferenceById(supportTicket.currentCustomerServiceOperatorId)

        def supportTicketData = new SupportTicketData(
                id: supportTicket.id,
                level: supportTicket.level.toString(),
                timeLimit: supportTicket.timeLimit.value(),
                title: supportTicket.title,
                status: supportTicket.status.toString(),
                customer: customer,
                currentCustomerServiceOperator: operator
        )
        supportTicketData.supportTicketRecordDataList = supportTicket.supportTicketRecordList.collect {supportTicketRecord ->
            def member = memberRepositoryPeer.getReferenceById(supportTicketRecord.posterId)

            new SupportTicketRecordData(
                    id: supportTicketRecord.id,
                    content: supportTicketRecord.content,
                    poster: member,
                    supportTicketData: supportTicketData
            )
        }
        return supportTicketData
    }

}
