package com.example.supportticketddd.member.repository


import com.example.supportticketddd.member.entity.Member
import com.example.supportticketddd.member.entity.Role
import com.example.supportticketddd.common.AbstractRepository

interface MemberRepository extends AbstractRepository<Member, Long> {
    List<Member> findAllByRole(Role role)
    Optional<Member> pickupRandom(Role role)

}