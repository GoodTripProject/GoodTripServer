package com.goodtrip.goodtripserver.authentication.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
@RequiredArgsConstructor
class JWTAuthenticationFilter : OncePerRequestFilter() {
    val jwtService = JwtService()
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
        TODO("extract username")
//        val username
    }
}