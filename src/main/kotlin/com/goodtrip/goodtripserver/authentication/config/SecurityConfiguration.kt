package com.goodtrip.goodtripserver.authentication.config

import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfiguration {
    @Autowired
    private lateinit var athenticationProvider: AuthenticationProvider

    @Autowired
    private lateinit var jwtAuthenticationFilter: JWTAuthenticationFilter//TODO если что поменять на просто JWTblablabla()

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { csrf -> csrf.disable() }
            .authorizeHttpRequests { requests ->
                requests.requestMatchers("auth/login").permitAll()
                    .requestMatchers("auth/register").permitAll()
                    .anyRequest().authenticated()
            }.sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authenticationProvider(athenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}