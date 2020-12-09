package org.hshekhar.surveyspot.config

import io.micronaut.context.annotation.Primary
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponseFactory
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpResponse
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.authentication.UserDetails
import io.micronaut.security.handlers.LoginHandler
import org.slf4j.LoggerFactory
import javax.inject.Singleton

/*
data class LoginResponse(val username: String?, val roles: List<String>?, val error: String? = null)

@Singleton
class SurveySpotLoginHandler : LoginHandler {
    private val LOGGER = LoggerFactory.getLogger(SurveySpotLoginHandler::class.java)

    override fun loginSuccess(userDetails: UserDetails?, request: HttpRequest<*>?): MutableHttpResponse<*> {
        LOGGER.debug("entry: loginSuccess()")
        return HttpResponseFactory.INSTANCE.ok(LoginResponse(
            username = userDetails?.username,
            roles = userDetails?.roles?.toList()
        ))
    }

    override fun loginRefresh(
        userDetails: UserDetails?,
        refreshToken: String?,
        request: HttpRequest<*>?
    ): MutableHttpResponse<*> {
        TODO("Not yet implemented")
    }

    override fun loginFailed(
        authenticationResponse: AuthenticationResponse?,
        request: HttpRequest<*>?
    ): MutableHttpResponse<*> {
        return HttpResponseFactory.INSTANCE.status(HttpStatus.UNAUTHORIZED, LoginResponse(
            username = null,
            roles = null,
            error = authenticationResponse?.message?.orElseGet {"Login Failed"}))
    }
}
*/