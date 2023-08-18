package com.example.supportticketddd.common

import org.springframework.context.ApplicationEvent

import java.time.Clock

abstract class DomainEvent extends ApplicationEvent{

    DomainEvent(Object source) {
        super(source)
    }

    DomainEvent(Object source, Clock clock) {
        super(source, clock)
    }
}