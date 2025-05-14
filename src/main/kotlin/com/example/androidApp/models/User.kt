package com.example.androidApp.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0


    @Column
    var name = ""


    val role = "USER"

    @Column(unique = true)
    var email = ""

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    val tickets: List<Ticket> = mutableListOf()

    @Column(name = "enabled")
    var enabled: Boolean = false
    @Column(unique = true)
    var phone = ""
    var otp: String? = null
    var otpExpiresAt: LocalDateTime? = null
    @Column
    var password = ""
        @JsonIgnore
        get() = field
        set(value) {
            field = passwordEncoder.encode(value)
        }

    companion object {
        private val passwordEncoder = BCryptPasswordEncoder()
    }

    fun checkPassword(password: String): Boolean {
        return passwordEncoder.matches(password, this.password)
    }


}
