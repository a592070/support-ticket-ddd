package com.example.supportticketddd.repository.member.memory

import com.example.supportticketddd.entity.member.Member
import com.example.supportticketddd.entity.member.Role
import com.example.supportticketddd.repository.member.MemberRepository
import org.springframework.stereotype.Repository

@Repository
class MemberInMemoryRepository implements MemberRepository{
    Map<Long, Member> memberMap = new HashMap<>()

    @Override
    List<Member> findAll() {
        return memberMap.values()
    }

    @Override
    Optional<Member> findById(Long id) {
        return Optional.ofNullable(memberMap.get(id))
    }

    @Override
    Long save(Member member) {
        if (member.id == null) {
            member.id = memberMap.keySet().stream().max {it}.map {it+1}.orElse(1)
        }
        memberMap.put(member.id, member)
        return member.id
    }

    @Override
    void deleteById(Long id) {
        findById(id).ifPresent {memberMap.remove(id) }
    }

    @Override
    List<Member> findAllByRole(Role role) {
        return memberMap.values().findAll {it.role == role}
    }

    @Override
    Optional<Member> pickupRandom(Role role) {
        return memberMap.values().stream().findAny()
    }
}
