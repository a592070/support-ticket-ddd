package com.example.supportticketddd

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class SupportTicketDddApplication {

    static void main(String[] args) {
        SpringApplication.run(SupportTicketDddApplication, args)
    }

}
