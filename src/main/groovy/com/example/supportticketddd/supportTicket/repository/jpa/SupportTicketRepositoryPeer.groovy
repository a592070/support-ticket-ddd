package com.example.supportticketddd.supportTicket.repository.jpa


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SupportTicketRepositoryPeer extends JpaRepository<SupportTicketData, Long>{

}