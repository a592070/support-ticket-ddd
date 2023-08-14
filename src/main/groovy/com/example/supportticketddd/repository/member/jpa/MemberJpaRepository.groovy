package com.example.supportticketddd.repository.member.jpa


import com.example.supportticketddd.entity.member.Member
import com.example.supportticketddd.entity.member.Role
import com.example.supportticketddd.repository.member.MemberRepository
import org.springframework.stereotype.Repository

import javax.annotation.Resource

@Repository
class MemberJpaRepository implements MemberRepository{

    @Resource
    MemberRepositoryPeer memberRepositoryPeer

    @Override
    List<Member> findAll() {
        return memberRepositoryPeer.findAll().collect {data ->
            convert(data)
        }
    }

    @Override
    Optional<Member> findById(Long id) {
        return memberRepositoryPeer.findById(id).map {data ->
            convert(data)
        }
    }

    @Override
    Long save(Member member) {
        memberRepositoryPeer.save(convert(member)).id
    }

    @Override
    void deleteById(Long id) {
        memberRepositoryPeer.deleteById(id)
    }

    @Override
    List<Member> findAllByRole(Role role) {
        return memberRepositoryPeer.findAllByRole(role.toString()).collect { data ->
            convert(data)
        }
    }

    @Override
    Optional<Member> pickupRandom(Role role) {
        def members = findAllByRole(role)
        return members.stream().findAny()
    }


    Member convert(MemberData memberData){
        return new Member(
                id: memberData.id,
                name: memberData.name,
                createDate: memberData.createDate,
//                createDate: memberData.createDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                lastModifiedDate: memberData.lastModifiedDate,
//                lastModifiedDate: memberData.lastModifiedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                role: Role.valueOf(memberData.role)
        )
    }
    MemberData convert(Member member){
        return new MemberData(
                id: member.id,
                name: member.name,
                createDate: member.createDate,
//                createDate: Date.from(member.createDate.atZone(ZoneId.systemDefault()).toInstant()),
                lastModifiedDate: member.createDate,
//                lastModifiedDate: Date.from(member.lastModifiedDate.atZone(ZoneId.systemDefault()).toInstant()),
                role: member.role
        )
    }
}
