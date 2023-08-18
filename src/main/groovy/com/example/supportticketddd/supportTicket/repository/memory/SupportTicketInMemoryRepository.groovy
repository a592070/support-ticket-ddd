package com.example.supportticketddd.supportTicket.repository.memory

import com.example.supportticketddd.supportTicket.entity.SupportTicket
import com.example.supportticketddd.supportTicket.repository.SupportTicketRepository
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
            supportTicket.id = supportTicketMap.keySet().stream().mapToLong {it}.max().orElse(0) +1
        }
        supportTicketMap.put(supportTicket.id, supportTicket)
        return supportTicket.id
    }

    @Override
    void deleteById(Long id) {
        supportTicketMap.remove(id)
    }
}
