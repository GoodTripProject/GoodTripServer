package com.goodtrip.goodtripserver.authentication.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.core.env.Environment
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*

@Service
class JwtService {

    private lateinit var environment: Environment

    fun extractUsername(token: String): String? = extractClaim(token, Claims::getSubject)

    fun <T> extractClaim(token: String, claimResolver: (Claims) -> T): T = claimResolver.invoke(extractAllClaims(token))

    private fun extractAllClaims(token: String): Claims =
        Jwts.parser().setSigningKey(getSignInKey()).build().parseClaimsJws(token).body
//    Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt) TODO заменить

    private fun getSignInKey(): Key {
        val keyBytes = getKeyBytes(environment.getProperty("SHA_SECRET_KEY")!!)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    private fun getKeyBytes(key: String) = Decoders.BASE64.decode(key)

    //TODO try to use non-deprecated methods
    fun generateToken(claims: Map<String, Any>, userDetails: UserDetails): String =
        Jwts.builder().setClaims(claims)
            .setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 24))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact()

    fun generateToken(userDetails: UserDetails) = generateToken(HashMap(), userDetails)

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username.equals(userDetails.username) && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String) = extractExpiration(token).before(Date())

    private fun extractExpiration(token: String) = extractClaim(token, Claims::getExpiration)
}