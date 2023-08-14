package com.example.supportticketddd.repository.member


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepositoryPeer extends JpaRepository<MemberData, Long>{
    List<MemberData> findAllByRole(String role)
}