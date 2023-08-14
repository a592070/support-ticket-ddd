package com.example.supportticketddd.repository.member

import groovy.transform.ToString
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener

import javax.annotation.Generated
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Version
import java.time.LocalDateTime

@Entity
@EntityListeners(AuditingEntityListener.class)
@ToString(includeNames = true)
class MemberData {
    @Id
    @GeneratedValue
    long id

    @Version
    long version

    @CreatedDate
    LocalDateTime createDate

    @LastModifiedDate
    LocalDateTime lastModifiedDate

    String name

    String role


}
