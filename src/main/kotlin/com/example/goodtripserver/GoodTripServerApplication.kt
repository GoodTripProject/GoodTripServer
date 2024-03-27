package com.example.goodtripserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories("com.goodtrip.goodtripserver.database.repositories")
class GoodTripServerApplication

fun main(args: Array<String>) {
    runApplication<GoodTripServerApplication>(*args)
}
