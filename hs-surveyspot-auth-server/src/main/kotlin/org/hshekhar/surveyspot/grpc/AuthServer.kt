package org.hshekhar.surveyspot.grpc

import com.google.protobuf.Empty
import com.nimbusds.jwt.JWTParser
import io.micronaut.security.annotation.Secured
import io.micronaut.security.token.jwt.generator.AccessRefreshTokenGenerator
import io.micronaut.security.token.jwt.validator.JwtTokenValidator
import org.hshekhar.surveyspot.proto.*
import org.hshekhar.surveyspot.service.UserRole
import org.hshekhar.surveyspot.service.UserService
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthServer: LoginAPIGrpcKt.LoginAPICoroutineImplBase() {

    @Inject
    private lateinit var userService: UserService

    @Inject
    private lateinit var tokenGenerator: AccessRefreshTokenGenerator

    companion object {
        private val LOGGER = LoggerFactory.getLogger(AuthServer::class.java)
    }

    override suspend fun logIn(request: LoginRequest): LoginResponse {
        val userDetail = userService.getUserByUserNameAndPassword(username = request.username, password = request.password)
        return if (null != userDetail) {
            val token = tokenGenerator.generate(userDetail).get()
            LoginResponse.newBuilder()
                .setToken(token.accessToken)
                .setUser(User.newBuilder()
                    .setId("demo")
                    .setUsername("Demo")
                    .build())
                .build()
        } else {
            LoginResponse.newBuilder()
                .setToken("")
                .build()
        }
    }

    override suspend fun logOut(request: LogoutRequest): Empty {
       /* val username = request.username
        val token = request.token*/
        return Empty.getDefaultInstance()
    }

    @Secured(value = [UserRole.EDITOR, UserRole.ADMIN])
    override suspend fun verifyToken(request: TokenVerificationRequest): TokenVerificationResponse {
        LOGGER.debug("entry: verifyToken()")
        val token = request.token
        return try {
            JWTParser.parse(token).jwtClaimsSet
            TokenVerificationResponse.newBuilder()
                .setValid(true)
                .build()
        } catch (e: Throwable) {
            TokenVerificationResponse.newBuilder()
                .setValid(false)
                .setError("Invalid token provided")
                .build()
        }
    }
}