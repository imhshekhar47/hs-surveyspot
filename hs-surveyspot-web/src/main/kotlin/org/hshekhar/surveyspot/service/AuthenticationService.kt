package org.hshekhar.surveyspot.service

import org.hshekhar.surveyspot.config.LoginGrpcClient
import org.hshekhar.surveyspot.dto.LoginRequestDTO
import org.hshekhar.surveyspot.dto.LoginResponseDTO
import org.hshekhar.surveyspot.proto.LoginRequest
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationService {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(AuthenticationService::class.java)
    }

    @Inject
    private lateinit var client: LoginGrpcClient

    fun login(request: LoginRequestDTO): LoginResponseDTO {
        LOGGER.trace("entry: login(request[username=${request.username}])")

        val response =  client.login(LoginRequest.newBuilder()
                .setUsername(request.username)
                .setPassword(request.password)
                .build())

        LOGGER.trace("exit: login()")
        return LoginResponseDTO(username = response.user.username,
                authToken = response.token,
                refreshToken = "")
    }
}