package com.example.goodtripserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
class GoodTripServerApplication

fun main(args: Array<String>) {
    runApplication<GoodTripServerApplication>(*args)
}
