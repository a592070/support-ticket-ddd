package com.example.supportticketddd.notification.event

import com.example.supportticketddd.common.exception.RepositoryEntityNotFoundException
import com.example.supportticketddd.member.entity.Member
import com.example.supportticketddd.member.repository.MemberRepository
import com.example.supportticketddd.notification.entity.Notification
import com.example.supportticketddd.notification.repository.NotificationRepository
import com.example.supportticketddd.supportTicket.entity.SupportTicket
import com.example.supportticketddd.supportTicket.entity.SupportTicketRecord
import com.example.supportticketddd.supportTicket.repository.SupportTicketRepository
import com.example.supportticketddd.supportTicket.usecase.openSupportTicket.SupportTicketOpened
import com.example.supportticketddd.supportTicket.usecase.replySupportTicket.SupportTicketReplied
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextStartedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

import javax.annotation.Resource

@Service
class NotificationEventHandler {

    @Resource(name = "notificationInMemoryRepository")
    NotificationRepository notificationRepository

    @Resource(name = "supportTicketJpaRepository")
    SupportTicketRepository supportTicketRepository

    @Resource(name = "memberJpaRepository")
    MemberRepository memberRepository

    @EventListener
    def handleSupportTicketOpened(SupportTicketOpened event){
        def supportTicket = supportTicketRepository.findById(event.supportTicketId).orElseThrow {
            new RepositoryEntityNotFoundException(SupportTicket, event.supportTicketId)
        }

        def notification = new Notification(
                senderId: supportTicket.customerId,
                receiverId: supportTicket.assignedOperatorId,
                message: "You are assigned a support ticket."
        )

        notificationRepository.save(notification)

        println "[${notification.id}]send message '${notification.message}' from ${notification.senderId} to ${notification.receiverId}"
    }


    @EventListener
    def handleSupportTicketReplied(SupportTicketReplied event){
        def supportTicket = supportTicketRepository.findById(event.supportTicketId).orElseThrow {
            new RepositoryEntityNotFoundException(SupportTicket, event.supportTicketId)
        }

        def lastRecord = supportTicket.getLastRecord()
        def poster = memberRepository.findById(lastRecord.posterId).orElseThrow {
            new RepositoryEntityNotFoundException(Member, lastRecord.posterId)
        }

        def notification
        if (poster.isCustomer()){
            notification = new Notification(
                    senderId: poster.id,
                    receiverId: supportTicket.assignedOperatorId,
                    message: "You received a reply from Customer."
            )
        }else{
            notification = new Notification(
                    senderId: poster.id,
                    receiverId: supportTicket.customerId,
                    message: "You received a reply from Operator."
            )
        }


        notificationRepository.save(notification)

        println "[${notification.id}]send message '${notification.message}' from ${notification.senderId} to ${notification.receiverId}"
    }


}
