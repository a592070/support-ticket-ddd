package com.example.supportticketddd.supportTicket.controller

import com.example.supportticketddd.supportTicket.usecase.openSupportTicket.OpenSupportTicketCommandHandler
import com.example.supportticketddd.supportTicket.usecase.query.ViewSupportTicketDto
import com.example.supportticketddd.supportTicket.usecase.query.ViewSupportTicketQuery
import com.example.supportticketddd.supportTicket.usecase.replySupportTicket.ReplySupportTicketCommandHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

import javax.annotation.Resource

@RestController("/ticket")
class SupportTicketController {

    @Resource
    OpenSupportTicketCommandHandler openSupportTicketCommandHandler

    @Resource
    ViewSupportTicketQuery viewSupportTicketQuery

    @Resource
    ReplySupportTicketCommandHandler replySupportTicketCommandHandler

    @PostMapping("/open")
    Long open(@RequestBody OpenSupportTicketDto openSupportTicketDto){
        openSupportTicketCommandHandler.execute(openSupportTicketDto.toCommand()).id
    }

    @PostMapping("/reply")
    Long reply(@RequestBody ReplySupportTicketDto replySupportTicketDto){
        replySupportTicketCommandHandler.execute(replySupportTicketDto.toCommand()).id
    }

    @GetMapping("/{id}")
    ViewSupportTicketDto view(@PathVariable long id){
        return viewSupportTicketQuery.execute(id)
    }

}
