package com.example.androidApp.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Entity
@Table(name = "users")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0


    @Column
    var name = ""


    @ManyToMany(fetch = FetchType.EAGER)
    val role: Set<Role> = emptySet()

    @Column(unique = true)
    var email = ""

    @Column(unique = true)
    var phone = ""

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

@Entity
data class Role(
    @Id @GeneratedValue val id: Int = 0,
    val name: String
)
