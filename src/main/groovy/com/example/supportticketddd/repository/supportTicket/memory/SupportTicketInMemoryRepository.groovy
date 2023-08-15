package com.example.supportticketddd.repository.supportTicket.memory

import com.example.supportticketddd.entity.supportTicket.SupportTicket
import com.example.supportticketddd.repository.supportTicket.SupportTicketRepository
import org.springframework.stereotype.Repository

@Repository
class SupportTicketInMemoryRepository implements SupportTicketRepository{
    Map<Long, SupportTicket> supportTicketMap = new HashMap<>()

    @Override
    List<SupportTicket> findAll() {
        return supportTicketMap.values()
    }

    @Override
    Optional<SupportTicket> findById(Long id) {
        return Optional.ofNullable(supportTicketMap.get(id))
    }

    @Override
    Long save(SupportTicket supportTicket) {
        if (supportTicket.id == null) {
            supportTicket.id = supportTicketMap.keySet().stream().max {it}.map {it+1}.orElse(1)
        }
        supportTicketMap.put(supportTicket.id, supportTicket)
        return supportTicket.id
    }

    @Override
    void deleteById(Long id) {
        supportTicketMap.remove(id)
    }
}