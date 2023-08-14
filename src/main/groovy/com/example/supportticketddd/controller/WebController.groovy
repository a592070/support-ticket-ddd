package com.example.supportticketddd.controller

import com.example.supportticketddd.repository.member.MemberData
import com.example.supportticketddd.repository.member.MemberRepositoryPeer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/web")
class WebController {

    @Autowired
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
