package com.example.supportticketddd.repository.supportTicket.memory

import com.example.supportticketddd.entity.supportTicket.SupportTicket
import com.example.supportticketddd.repository.supportTicket.SupportTicketRepository
import org.springframework.stereotype.Repository

@Repository
class SupportTicketInMemoryRepository implements SupportTicketRepository{
    List<SupportTicket> supportTicketList = new ArrayList<>()

    @Override
    List<SupportTicket> findAll() {
        return supportTicketList
    }

    @Override
    Optional<SupportTicket> findById(Long id) {
        return Optional.ofNullable(supportTicketList.find {it.id == id })
    }

    @Override
    Long save(SupportTicket supportTicket) {
        if (findById(supportTicket.id).isPresent()) {
            supportTicketList.set(supportTicket.id as int, supportTicket)
        }else{
            supportTicketList.add(supportTicket)
        }
        return supportTicketList.indexOf(supportTicket)
    }

    @Override
    void deleteById(Long id) {
        supportTicketList.remove(id)
    }
}
