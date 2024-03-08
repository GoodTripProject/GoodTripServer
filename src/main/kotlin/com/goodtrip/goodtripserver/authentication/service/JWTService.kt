package com.goodtrip.goodtripserver.authentication.service

import com.goodtrip.goodtripserver.authentication.model.RegistrationRequest
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import java.util.*


class JWTService {
    @Value("\${token.signing.key}")
    private lateinit var jwtSigningKey: String //TODO

    /**
     * Extract expiration date from token
     *
     * @param token token
     * @return expiration data
     */
    private fun extractExpiration(token: String): Date {
        return extractClaim(token) { obj: Claims -> obj.expiration }
    }


    /**
     * Extract all data from token
     *
     * @param token token
     * @return data from token
     */
    private fun extractAllClaims(token: String) =
        Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
            .body


    /**
     * Extract date from token
     *
     * @param token           token
     * @param claimsResolvers function that extract data
     * @return data
    </T> */
    private fun <T> extractClaim(token: String, claimsResolvers: (Claims) -> T): T {
        val claims: Claims = extractAllClaims(token)
        return claimsResolvers(claims)
    }

    /**
     * Get key for token
     *
     * @return key
     */
    private fun getSigningKey() = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSigningKey))


    /**
     * Extract username from token
     *
     * @param token token
     * @return username
     */
    fun extractUserName(token: String): String = extractClaim(token, Claims::getSubject)

    /**
     * Generate token
     *
     * @param extraClaims optional data
     * @param userDetails user data
     * @return token
     */
    private fun generateToken(extraClaims: Map<String, Any?>, userDetails: RegistrationRequest) =
        Jwts.builder().setClaims(extraClaims).setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 100000 * 60 * 24))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact()

    /**
     * Generate token
     *
     * @param userDetails user data
     * @return token
     */
    fun generateToken(userDetails: RegistrationRequest): String {
        val claims: MutableMap<String, Any> = HashMap()
        claims["username"] = userDetails.username
        claims["name"] = userDetails.name
        claims["surname"] = userDetails.surname
        claims["handle"] = userDetails.handle
        claims["password"] = userDetails.password

        return generateToken(claims, userDetails)
    }

    /**
     * Check if token is expired
     *
     * @param token token
     * @return true, if token is expired
     */
    private fun isTokenExpired(token: String) = extractExpiration(token).before(Date())


    /**
     * Check the token for validity
     *
     * @param token       token
     * @param userDetails user data
     * @return true, if token is valid
     */
    fun isTokenValid(token: String, userDetails: UserDetails) =
        (extractUserName(token) == userDetails.username) && !isTokenExpired(token)
}