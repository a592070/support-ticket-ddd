package com.example.supportticketddd.repository.member

import com.example.supportticketddd.entity.member.Customer
import com.example.supportticketddd.entity.member.CustomerServiceOperator
import com.example.supportticketddd.entity.member.Member
import com.example.supportticketddd.entity.member.Role
import com.example.supportticketddd.repository.AbstractRepository
import org.springframework.stereotype.Repository

interface MemberRepository extends AbstractRepository<Member, Long> {
    List<Member> findAllByRole(Role role)
    Member pickupRandom(Role role)

}