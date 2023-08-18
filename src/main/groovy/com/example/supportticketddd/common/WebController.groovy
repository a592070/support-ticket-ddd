package com.example.supportticketddd.common

import com.example.supportticketddd.member.repository.jpa.MemberData
import com.example.supportticketddd.member.repository.jpa.MemberRepositoryPeer
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.annotation.Resource

@RestController("/web")
class WebController {

    @Resource
    MemberRepositoryPeer memberRepositoryPeer

    @RequestMapping("/initialData")
    void initialData(){
        memberRepositoryPeer.saveAll(
                [
                        new MemberData(
                                name: "client1",
                                role: "CUSTOMER"
                        ),
                        new MemberData(
                                name: "operator1",
                                role: "CUSTOMER_SERVICE_OPERATOR"
                        ),
                        new MemberData(
                                name: "operator2",
                                role: "CUSTOMER_SERVICE_OPERATOR"
                        ),
                        new MemberData(
                                name: "manager1",
                                role: "CUSTOMER_SERVICE_MANAGER"
                        ),
                ]

        )


    }
}
