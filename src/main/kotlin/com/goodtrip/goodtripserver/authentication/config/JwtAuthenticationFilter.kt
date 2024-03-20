package com.goodtrip.goodtripserver.authentication.config

import com.goodtrip.goodtripserver.authentication.service.UserService
import com.goodtrip.goodtripserver.authentication.service.UserServiceImpl
import io.jsonwebtoken.io.IOException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import kotlin.jvm.Throws

@Component
@RequiredArgsConstructor
class JwtAuthenticationFilter : OncePerRequestFilter() {
    @Autowired
    private lateinit var jwtService : JwtService

    @Autowired
    private lateinit var userService : UserService

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val authHeader = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response)
            return
        }
        //Extract jwt token from header
        val jwt: String = authHeader.substring(7)
        val username = jwtService.extractUsername(jwt)
        println("jwt = $jwt");
        println("username = $username");
        //check that user is not connected yet
        if (!username.isNullOrEmpty() && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userService.loadUserByUsername(username)
            if (jwtService.isTokenValid(jwt, userDetails)) {
                println("jwt token is valid")
                val authToken = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities
                )

                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)

                // Update security context holder
                SecurityContextHolder.getContext().authentication = authToken
            }
        }

        println("perform filter chain")
        filterChain.doFilter(request, response)

    }
}