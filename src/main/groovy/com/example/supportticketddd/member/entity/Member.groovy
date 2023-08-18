package com.example.supportticketddd.member.entity

import com.example.supportticketddd.common.AggregateRoot
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
