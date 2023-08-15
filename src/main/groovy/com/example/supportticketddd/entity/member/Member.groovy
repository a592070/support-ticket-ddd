package com.example.supportticketddd.entity.member

import com.example.supportticketddd.entity.AggregateRoot
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import java.time.LocalDateTime

@ToString(includeNames = true)
@EqualsAndHashCode(includes = "id")
class Member extends AggregateRoot{
    Long id
    String name

    LocalDateTime createDate
    LocalDateTime lastModifiedDate

    Role role
}
