package com.goodtrip.goodtripserver.authentication.config

import com.goodtrip.goodtripserver.authentication.service.UserService
import com.goodtrip.goodtripserver.authentication.service.UserServiceImpl
import com.goodtrip.goodtripserver.database.repositories.AuthenticationRepository
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


@Configuration
@RequiredArgsConstructor
@EnableJpaRepositories("com.goodtrip.goodtripserver.database.repositories")
@ComponentScan(basePackages = ["com.goodtrip.goodtripserver.database.configs"])
@EntityScan(basePackages = ["com.goodtrip.goodtripserver.database.models"])
@ComponentScan
class ApplicationConfig {
    @Autowired
    private lateinit var authenticationRepository: AuthenticationRepository

    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService { username: String ->
            authenticationRepository.getUserByUsername(username)
                .orElseThrow { UsernameNotFoundException("User not found") }
        }
    }

    @Bean
    fun userService(): UserService {
        return UserServiceImpl();
    }

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService())
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager = config.authenticationManager

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

}