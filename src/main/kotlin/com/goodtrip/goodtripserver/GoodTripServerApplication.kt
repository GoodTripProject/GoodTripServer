package com.example.goodtripserver

import com.goodtrip.goodtripserver.authentication.config.SecurityConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan


@SpringBootApplication(
    exclude = [JdbcTemplateAutoConfiguration::class, SecurityAutoConfiguration::class],
    scanBasePackageClasses = [SecurityConfiguration::class]
)
@ComponentScan(basePackages = ["com.goodtrip.goodtripserver"])
class GoodTripServerApplication

fun main(args: Array<String>) {
    runApplication<GoodTripServerApplication>(*args)
}
