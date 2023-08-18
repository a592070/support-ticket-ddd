package com.example.supportticketddd.member.repository.memory

import com.example.supportticketddd.member.entity.Member
import com.example.supportticketddd.member.entity.Role
import com.example.supportticketddd.member.repository.MemberRepository
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
            member.id = memberMap.keySet().stream().mapToLong {it}.max().orElse(0) +1
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
