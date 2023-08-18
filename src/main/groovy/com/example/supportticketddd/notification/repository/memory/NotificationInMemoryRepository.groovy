package com.example.supportticketddd.notification.repository.memory

import com.example.supportticketddd.notification.entity.Notification
import com.example.supportticketddd.notification.repository.NotificationRepository
import org.springframework.stereotype.Repository

@Repository
class NotificationInMemoryRepository implements NotificationRepository{
    Map<Long, Notification> storeMap = new HashMap<>()

    @Override
    List<Notification> findAll() {
        return storeMap.values()
    }

    @Override
    Optional<Notification> findById(Long id) {
        return Optional.of(storeMap.get(id))
    }

    @Override
    Long save(Notification notification) {
        if (notification.id == null) {
            notification.id = storeMap.keySet().stream().mapToLong {it}.max().orElse(0) + 1
        }
        storeMap.put(notification.id, notification)
        return notification.id
    }

    @Override
    void deleteById(Long id) {
        storeMap.remove(id)
    }
}
