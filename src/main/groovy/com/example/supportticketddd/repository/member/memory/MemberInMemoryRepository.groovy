package com.example.supportticketddd.repository.member.memory

import com.example.supportticketddd.entity.member.Member
import com.example.supportticketddd.entity.member.Role
import com.example.supportticketddd.repository.member.MemberRepository
import org.springframework.stereotype.Repository

@Repository
class MemberInMemoryRepository implements MemberRepository{
    List<Member> memberList = new ArrayList<>()

    @Override
    List<Member> findAll() {
        return memberList
    }

    @Override
    Optional<Member> findById(Long id) {
        return Optional.ofNullable(memberList.find {it.id == id})
    }

    @Override
    Long save(Member member) {
        if (findById(member.id).isPresent()) {
            memberList.set(member.id as int, member)
        }else{
            memberList.add(member)
        }
        return memberList.indexOf(member)
    }

    @Override
    void deleteById(Long id) {
        findById(id).ifPresent {memberList.remove(id) }
    }

    @Override
    List<Member> findAllByRole(Role role) {
        return memberList.findAll {it.role == role}
    }

    @Override
    Optional<Member> pickupRandom(Role role) {
        return memberList.stream().findAny()
    }
}
