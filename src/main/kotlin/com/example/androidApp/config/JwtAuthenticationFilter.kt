package com.example.androidApp.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.security.SignatureException
import javax.crypto.SecretKey

class JwtAuthenticationFilter : OncePerRequestFilter() {

    private val secretKey: SecretKey = Keys.hmacShaKeyFor("your-very-secret-key-your-secret-key-here".toByteArray())

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.getHeader("Authorization")
            ?.takeIf { it.startsWith("Bearer ") }
            ?.substringAfter("Bearer ")

        if (token != null) {
            try {
                val claims: Claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .body

                val email = claims["email"] as? String ?: claims.subject
                val role = claims["role"] as? String
                if (role == null || role.isBlank()){
                    SecurityContextHolder.clearContext()
                    response.status = HttpServletResponse.SC_FORBIDDEN
                    return
                }
                val authorities = listOf(SimpleGrantedAuthority(role))

                val authentication = UsernamePasswordAuthenticationToken(email, null, authorities)
                SecurityContextHolder.getContext().authentication = authentication


            } catch (e: ExpiredJwtException) {
                println("❌ Token expired")
                SecurityContextHolder.clearContext()
            } catch (e: SignatureException) {
                println("❌ Token signature invalid")
                SecurityContextHolder.clearContext()
            } catch (e: Exception) {
                println("❌ Token parse error: ${e.message}")
                SecurityContextHolder.clearContext()
            }
        }

        filterChain.doFilter(request, response)
    }
}
