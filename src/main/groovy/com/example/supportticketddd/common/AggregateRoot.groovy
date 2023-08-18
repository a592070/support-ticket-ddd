package com.example.supportticketddd.common

import java.util.concurrent.CopyOnWriteArrayList

abstract class AggregateRoot {

    private final List<DomainEvent> domainEvents = new CopyOnWriteArrayList<>()

    void addDomainEvents(DomainEvent domainEvent){
        domainEvents.add(domainEvent)
    }

    List<DomainEvent> getDomainEvents(){
        Collections.unmodifiableList(domainEvents)
    }

    void clearDomainEvents(){
        domainEvents.clear()
    }


}
