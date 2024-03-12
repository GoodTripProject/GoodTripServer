package com.goodtrip.goodtripserver.authentication.config

import com.goodtrip.goodtripserver.authentication.repository.UserRepository
import com.goodtrip.goodtripserver.database.repositories.AuthenticationRepository
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


@Configuration
@RequiredArgsConstructor
class ApplicationConfig {
    @Autowired
    private lateinit var authenticationRepository: AuthenticationRepository


    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService { username: String ->
           authenticationRepository.getUserByEmail(username).orElseThrow { UsernameNotFoundException("User not found") }
        }
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