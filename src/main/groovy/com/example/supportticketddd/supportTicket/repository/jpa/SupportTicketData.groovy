package com.example.supportticketddd.supportTicket.repository.jpa

import com.example.supportticketddd.member.repository.jpa.MemberData
import groovy.transform.ToString
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Version
import java.time.LocalDateTime

@Entity
@EntityListeners(AuditingEntityListener.class)
@ToString(includeNames = true)
class SupportTicketData {
    @Id
    @GeneratedValue
    long id

    @Version
    long version

    @CreatedDate
    LocalDateTime createDate

    @LastModifiedDate
    LocalDateTime lastModifiedDate

    String level
    Long timeLimit
    String title
    String status

    @OneToOne
    MemberData customer

    @OneToOne
    MemberData assignedOperator

    @OneToMany(cascade = CascadeType.ALL)
    List<SupportTicketRecordData> supportTicketRecordDataList


}
