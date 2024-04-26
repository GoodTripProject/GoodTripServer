package com.goodtrip.goodtripserver

import com.goodtrip.goodtripserver.authentication.config.SecurityConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication


@SpringBootApplication(
    exclude = [JdbcTemplateAutoConfiguration::class, SecurityAutoConfiguration::class],
    scanBasePackageClasses = [SecurityConfiguration::class]
)
class GoodTripServerApplication

fun main(args: Array<String>) {
    runApplication<GoodTripServerApplication>(*args)
}
