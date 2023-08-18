package com.example.supportticketddd.supportTicket.controller

import com.example.supportticketddd.supportTicket.usecase.openSupportTicket.OpenSupportTicketCommand
import com.example.supportticketddd.supportTicket.usecase.openSupportTicket.OpenSupportTicketDto
import com.example.supportticketddd.supportTicket.usecase.query.ViewSupportTicketDto
import com.example.supportticketddd.supportTicket.usecase.query.ViewSupportTicketQuery
import com.example.supportticketddd.supportTicket.usecase.replySupportTicket.ReplySupportTicketCommand
import com.example.supportticketddd.supportTicket.usecase.replySupportTicket.ReplySupportTicketDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

import javax.annotation.Resource

@RestController("/ticket")
class SupportTicketController {

    @Resource
    OpenSupportTicketCommand openSupportTicketCommand

    @Resource
    ViewSupportTicketQuery viewSupportTicketQuery

    @Resource
    ReplySupportTicketCommand replySupportTicketCommand

    @PostMapping("/open")
    Long open(@RequestBody OpenSupportTicketDto openSupportTicketDto){
        openSupportTicketCommand.exec(openSupportTicketDto)
    }

    @PostMapping("/reply")
    Long reply(@RequestBody ReplySupportTicketDto replySupportTicketDto){
        replySupportTicketCommand.exec(replySupportTicketDto)
    }

    @GetMapping("/{id}")
    ViewSupportTicketDto view(@PathVariable long id){
        return viewSupportTicketQuery.exec(id)
    }

}