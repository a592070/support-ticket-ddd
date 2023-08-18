package com.example.supportticketddd.notification.entity

import com.example.supportticketddd.common.AggregateRoot

class Notification extends AggregateRoot{
    Long id

    String message

    Long senderId
    Long receiverId

}
