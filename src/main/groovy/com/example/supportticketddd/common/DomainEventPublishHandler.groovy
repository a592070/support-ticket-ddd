package com.example.supportticketddd.common

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

import javax.annotation.Resource

@Component
class DomainEventPublishHandler {

    @Resource
    ApplicationEventPublisher applicationEventPublisher


    void publishAll(AggregateRoot aggregateRoot){
        aggregateRoot.getDomainEvents().each {
            applicationEventPublisher.publishEvent(it)
        }
        aggregateRoot.clearDomainEvents()
    }

}