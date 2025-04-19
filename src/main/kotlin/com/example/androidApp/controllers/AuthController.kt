package com.example.androidApp.controllers

import com.example.androidApp.dtos.*
import com.example.androidApp.models.User
import com.example.androidApp.services.UserService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import com.google.firebase.auth.UserRecord
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import org.springframework.http.ResponseEntity
import java.util.*
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.security.SignatureException
import javax.crypto.SecretKey


@RestController
@RequestMapping
@CrossOrigin(origins = ["http://localhost:3000", "http://localhost:4200"])

class AuthController(
    private val userService: UserService
) {

    private val secretKey: SecretKey = Keys.hmacShaKeyFor("your-very-secret-key-your-secret-key-here".toByteArray())

    @GetMapping("/user/info")
    fun user(): ResponseEntity<Any> {
        // Lấy thông tin người dùng từ SecurityContextHolder
        val authentication = SecurityContextHolder.getContext().authentication
        val email = authentication?.principal as? String

        if (email.isNullOrBlank()) {
            return ResponseEntity.status(401).body(Message("Unauthenticated"))
        }

        // Lấy người dùng từ service của bạn
        val user = userService.findByEmail(email)
            ?: return ResponseEntity.status(404).body(Message("User not found"))

        // Trả về thông tin người dùng
        return ResponseEntity.ok(UserDTO(user.id, user.name, user.email, user.phone))
    }

    @PostMapping("auth/signup")
    fun signup(@RequestBody body: RegisterDTO): ResponseEntity<Any> {
        val error = when {
            userService.existsByEmail(body.email) -> "Email đã được sử dụng!"
            userService.existsByPhone(body.phone) -> "Số điện thoại đã được sử dụng!"
            else -> null
        }

        return error?.let { ResponseEntity.badRequest().body(Message(it)) }
            ?: run {

                val user = User().apply {
                    name = body.name
                    email = body.email
                    phone = body.phone
                    password = body.password
                }

                userService.save(user)

                val firebaseUser = FirebaseAuth.getInstance().createUser(
                    UserRecord.CreateRequest()
                        .setEmail(body.email)
                        .setPassword(body.password)
                        .setPhoneNumber("+84" + body.phone)
                        .setDisplayName(body.name)
                )


                ResponseEntity.ok(Message("Đăng ký thành công!"))
            }
    }

    @PostMapping("/auth/login")
    fun login(@RequestBody body: LoginDTO, response: HttpServletResponse): ResponseEntity<Any> {
        val user = this.userService.findByEmail(body.email)
            ?: return ResponseEntity.badRequest().body(Message("Không tồn tại người dùng!"))
        if (!user.checkPassword(body.password)) {
            return ResponseEntity.badRequest().body(Message("Mật khẩu không đúng "))
        }
        val role = user.role.firstOrNull() ?: "USER"

        val jwt = Jwts.builder()
            .setSubject(user.id.toString())
            .claim("email", user.email)
            .claim("role", "ROLE_$role")
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis()+1000*20*60*24))
            .signWith(secretKey)
            .compact()

        var cookie = Cookie("jwt", jwt)
        cookie.isHttpOnly = true

        response.addCookie(cookie)
//        val customToken = FirebaseAuth.getInstance().createCustomToken(user.id.toString())

        return ResponseEntity.ok(AuthDTO(jwt, user.id))

    }


}