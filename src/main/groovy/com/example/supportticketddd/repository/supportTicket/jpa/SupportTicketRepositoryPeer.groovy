package com.example.supportticketddd.repository.supportTicket.jpa

import com.example.supportticketddd.repository.supportTicket.jpa.SupportTicketData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SupportTicketRepositoryPeer extends JpaRepository<SupportTicketData, Long>{

}