package com.goodtrip.goodtripserver.authentication.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
@RequiredArgsConstructor
class JWTAuthenticationFilter : OncePerRequestFilter() {
    val jwtService = JwtService()
    private lateinit var userService: UserDetailsService;
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
        if (username != null /*&& username.isNotEmpty()*/ && SecurityContextHolder.getContext().authentication == null) {//check that user is not connected yet
            val userDetails = userService.loadUserByUsername(username)
            if (jwtService.isTokenValid(jwt, userDetails)) {
                val authToken = UsernamePasswordAuthenticationToken(userService, null, userDetails.authorities)

                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                //update security context holder
                SecurityContextHolder.getContext().authentication = authToken
            }
            filterChain.doFilter(request, response)
        }
    }
}