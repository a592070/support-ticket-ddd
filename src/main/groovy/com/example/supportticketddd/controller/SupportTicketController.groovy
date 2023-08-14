package com.example.supportticketddd.controller

import com.example.supportticketddd.usecase.openSupportTicket.OpenSupportTicketCommand
import com.example.supportticketddd.usecase.openSupportTicket.OpenSupportTicketDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/ticket")
class SupportTicketController {

    @Autowired
    OpenSupportTicketCommand openSupportTicketCommand


    @PostMapping("/open")
    void open(@RequestBody OpenSupportTicketDto openSupportTicketDto){
        openSupportTicketCommand.exec(openSupportTicketDto)
    }

}
