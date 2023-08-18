package com.example.supportticketddd.member.repository.jpa


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepositoryPeer extends JpaRepository<MemberData, Long>{
    List<MemberData> findAllByRole(String role)
}