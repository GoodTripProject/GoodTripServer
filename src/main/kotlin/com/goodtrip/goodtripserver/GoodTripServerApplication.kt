package com.goodtrip.goodtripserver

import RsaProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(RsaProperties::class)//TODO глянуть, работает ли
class GoodTripServerApplication

fun main(args: Array<String>) {
    runApplication<GoodTripServerApplication>(*args)
}
