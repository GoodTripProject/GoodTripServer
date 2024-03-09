package com.goodtrip.goodtripserver.authentication.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.security.Key

@Service
class JwtService {
    companion object {
        const val SECRET_KEY =
            "EF6B58B503DAB031564EEDC7E14088E0938F36C87734E771333F1EEF8F31BE35" //TODO не забыть удалить точно
    }

    fun extractUsername(jwt: String): String {
        TODO()
    }

    private fun extractAllClaims(token: String): Claims =
        Jwts.parser().setSigningKey(getSignInKey()).build().parseClaimsJws(token).body
//    Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt) TODO заменить

    private fun getSignInKey(): Key {
        val keyBytes = Decoders.BASE64.decode(SECRET_KEY)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}