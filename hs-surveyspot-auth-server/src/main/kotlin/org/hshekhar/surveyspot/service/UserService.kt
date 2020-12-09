package org.hshekhar.surveyspot.service

import io.micronaut.security.authentication.UserDetails
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Singleton

class UserRole {
    companion object {
        const val EDITOR = "EDITOR"
        const val ADMIN = "ADMIN"
    }
}

data class User(val username: String, val password: String, val roles: List<String>)

@Singleton
class UserService {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(UserService::class.java)
        private val ENCODER = Base64.getEncoder()
    }

    private val users = listOf<User>(
        User(username = "demo", password = "ZGVtbw==", roles = listOf()),
        User(username = "editor", password = "ZWRpdG9y", roles = listOf(UserRole.EDITOR)),
        User(username = "admin", password = "YWRtaW4=", roles = listOf(UserRole.ADMIN))
    )

    fun getUserByUserNameAndPassword(username: String, password: String): UserDetails? {
        val encodedPassword = ENCODER.encodeToString(password.toByteArray())
        LOGGER.debug("Looking user $username with password $encodedPassword")
        return users
            .firstOrNull { it.username == username && it.password == encodedPassword }
            ?.let { UserDetails(it.username, it.roles) }
    }
}